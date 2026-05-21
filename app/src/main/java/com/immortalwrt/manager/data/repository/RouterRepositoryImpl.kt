package com.immortalwrt.manager.data.repository

import com.immortalwrt.manager.core.network.CertificateFingerprintStore
import com.immortalwrt.manager.core.network.CertificateTrustManager
import com.immortalwrt.manager.core.network.EndpointDiscovery
import com.immortalwrt.manager.core.network.EndpointProbeResult
import com.immortalwrt.manager.core.network.Layer1Result
import com.immortalwrt.manager.core.network.Layer2Result
import com.immortalwrt.manager.core.network.Layer3Result
import com.immortalwrt.manager.core.security.SecretStore
import com.immortalwrt.manager.data.local.dao.DiagnosticEventDao
import com.immortalwrt.manager.data.local.dao.RouterCacheDao
import com.immortalwrt.manager.data.local.dao.RouterCapabilityDao
import com.immortalwrt.manager.data.local.dao.RouterDao
import com.immortalwrt.manager.data.local.dao.RouterEnvironmentDao
import com.immortalwrt.manager.data.local.entity.RouterCapabilityEntity
import com.immortalwrt.manager.data.local.entity.RouterEntity
import com.immortalwrt.manager.data.local.entity.RouterEnvironmentEntity
import com.immortalwrt.manager.domain.model.AppError
import com.immortalwrt.manager.domain.model.AppResult
import com.immortalwrt.manager.domain.model.EndpointMode
import com.immortalwrt.manager.domain.model.Router
import com.immortalwrt.manager.domain.model.RouterScheme
import com.immortalwrt.manager.domain.model.RouterSecurityContext
import com.immortalwrt.manager.domain.repository.RouterRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Singleton
class RouterRepositoryImpl @Inject constructor(
    private val routerDao: RouterDao,
    private val routerEnvironmentDao: RouterEnvironmentDao,
    private val routerCapabilityDao: RouterCapabilityDao,
    private val routerCacheDao: RouterCacheDao,
    private val diagnosticEventDao: DiagnosticEventDao,
    private val secretStore: SecretStore,
    private val endpointDiscovery: EndpointDiscovery,
    @Suppress("UNUSED_PARAMETER") private val certificateTrustManager: CertificateTrustManager,
    private val certificateFingerprintStore: CertificateFingerprintStore
) : RouterRepository {

    override fun observeAll(): Flow<List<Router>> {
        return routerDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getById(id: String): Router? {
        return withContext(Dispatchers.IO) {
            routerDao.getById(id)?.toDomain()
        }
    }

    override suspend fun addRouter(router: Router, password: String): AppResult<Router> {
        return withContext(Dispatchers.IO) {
            try {
                val id = router.id.ifEmpty { UUID.randomUUID().toString() }
                val secretAlias = "router_${id}_password"

                secretStore.store(secretAlias, password.encodeToByteArray())

                val routerWithRef = router.copy(
                    id = id,
                    passwordSecretRef = secretAlias
                )
                routerDao.upsert(routerWithRef.toEntity())
                AppResult.Success(routerWithRef)
            } catch (e: Exception) {
                AppResult.Failure(AppError.Unknown(e))
            }
        }
    }

    override suspend fun updateRouter(router: Router, password: String?): AppResult<Router> {
        return withContext(Dispatchers.IO) {
            try {
                val resolvedRef = if (password != null) {
                    val alias = router.passwordSecretRef ?: "router_${router.id}_password"
                    secretStore.store(alias, password.encodeToByteArray())
                    alias
                } else {
                    router.passwordSecretRef
                        ?: routerDao.getById(router.id)?.passwordSecretRef
                        ?: "router_${router.id}_password"
                }

                val updatedRouter = router.copy(passwordSecretRef = resolvedRef)
                routerDao.upsert(updatedRouter.toEntity())
                AppResult.Success(updatedRouter)
            } catch (e: Exception) {
                AppResult.Failure(AppError.Unknown(e))
            }
        }
    }

    override suspend fun deleteRouter(id: String): AppResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                routerDao.deleteById(id)
                routerEnvironmentDao.deleteById(id)
                routerCapabilityDao.deleteById(id)
                routerCacheDao.deleteSystemSnapshot(id)
                routerCacheDao.deleteInterfaceCaches(id)
                routerCacheDao.deleteDeviceCaches(id)
                diagnosticEventDao.deleteByRouter(id)
                secretStore.delete("router_${id}_password")
                certificateFingerprintStore.removeFingerprint(id)
                AppResult.Success(Unit)
            } catch (e: Exception) {
                AppResult.Failure(AppError.Unknown(e))
            }
        }
    }

    override suspend fun testConnection(routerId: String): AppResult<EndpointProbeResult> {
        return withContext(Dispatchers.IO) {
            try {
                val entity = routerDao.getById(routerId)
                    ?: return@withContext AppResult.Failure(AppError.Unknown(null))

                @Suppress("UNUSED_VARIABLE")
                val securityContext = RouterSecurityContext(
                    routerId = entity.id,
                    scheme = entity.scheme,
                    host = entity.host,
                    port = entity.port,
                    certificateFingerprint = entity.certificateFingerprint,
                    httpRiskAccepted = entity.httpRiskAccepted
                )

                val probeResult = endpointDiscovery.probeEndpoint(
                    host = entity.host,
                    port = entity.port,
                    scheme = entity.scheme
                )

                if (probeResult.layer1Connectivity is Layer1Result.Reachable) {
                    val availableEndpoint = probeResult.layer2Endpoints.entries
                        .firstOrNull { it.value is Layer2Result.Available }

                    if (availableEndpoint != null) {
                        val passwordBytes = secretStore.retrieve(entity.passwordSecretRef)

                        if (passwordBytes != null) {
                            val password = passwordBytes.decodeToString()

                            val authResult = endpointDiscovery.probeAuthAndCapability(
                                host = entity.host,
                                port = entity.port,
                                scheme = entity.scheme,
                                endpoint = availableEndpoint.key,
                                username = entity.username,
                                password = password
                            )

                            routerEnvironmentDao.upsert(
                                RouterEnvironmentEntity(
                                    routerId = routerId,
                                    distribution = null,
                                    version = null,
                                    revision = null,
                                    kernelVersion = null,
                                    luciAvailable = null,
                                    rpcdAvailable = null,
                                    endpointMode = resolveEndpointMode(availableEndpoint.key)
                                )
                            )

                            routerCapabilityDao.upsert(
                                buildCapabilityEntity(routerId, authResult)
                            )

                            return@withContext AppResult.Success(
                                probeResult.copy(layer3Auth = authResult)
                            )
                        }
                    }
                }

                AppResult.Success(probeResult)
            } catch (e: Exception) {
                AppResult.Failure(AppError.Unknown(e))
            }
        }
    }

    private fun resolveEndpointMode(endpointPath: String): EndpointMode {
        return when {
            endpointPath.contains("/ubus") -> EndpointMode.DIRECT_UBUS
            endpointPath.contains("luci") -> EndpointMode.LUCI_PROXY
            else -> EndpointMode.CUSTOM
        }
    }

    private fun buildCapabilityEntity(
        routerId: String,
        authResult: Layer3Result
    ): RouterCapabilityEntity {
        val knownCapabilities = when (authResult) {
            is Layer3Result.Success -> CAPABILITY_PROBE_LABELS.associateWith { true }
            is Layer3Result.MinCapabilityFailed -> CAPABILITY_PROBE_LABELS.associate { label ->
                label to (label !in authResult.missing)
            }
            else -> emptyMap()
        }

        return RouterCapabilityEntity(
            routerId = routerId,
            hasSessionLogin = authResult is Layer3Result.Success,
            hasSystemBoard = knownCapabilities["system.board"] ?: false,
            hasSystemInfo = knownCapabilities["system.info"] ?: false,
            hasNetworkInterfaceDump = knownCapabilities["network.interface.dump"] ?: false
        )
    }

    companion object {
        private val CAPABILITY_PROBE_LABELS = listOf(
            "system.board",
            "system.info",
            "network.interface.dump"
        )
    }
}

private fun RouterEntity.toDomain(): Router = Router(
    id = id,
    name = name,
    host = host,
    port = port,
    scheme = scheme,
    endpoint = endpoint,
    username = username,
    passwordSecretRef = passwordSecretRef,
    certificateFingerprint = certificateFingerprint,
    lastSuccessAt = lastSuccessAt,
    lastFailureReason = lastFailureReason,
    createdAt = createdAt,
    updatedAt = updatedAt
)

private fun Router.toEntity(): RouterEntity = RouterEntity(
    id = id,
    name = name,
    host = host,
    port = port,
    scheme = scheme,
    endpoint = endpoint,
    username = username,
    passwordSecretRef = passwordSecretRef ?: "",
    certificateFingerprint = certificateFingerprint,
    httpRiskAccepted = scheme == RouterScheme.HTTP,
    lastSuccessAt = lastSuccessAt,
    lastFailureReason = lastFailureReason,
    createdAt = createdAt,
    updatedAt = System.currentTimeMillis()
)
