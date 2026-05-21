package com.immortalwrt.manager.data.repository

import com.immortalwrt.manager.core.network.UbusJsonRpcClient
import com.immortalwrt.manager.data.local.dao.RouterCapabilityDao
import com.immortalwrt.manager.data.local.dao.RouterEnvironmentDao
import com.immortalwrt.manager.data.local.entity.RouterCapabilityEntity
import com.immortalwrt.manager.data.local.entity.RouterEnvironmentEntity
import com.immortalwrt.manager.domain.model.AppError
import com.immortalwrt.manager.domain.model.AppResult
import com.immortalwrt.manager.domain.model.EndpointMode
import com.immortalwrt.manager.domain.model.RouterCapability
import com.immortalwrt.manager.domain.model.RouterEnvironment
import com.immortalwrt.manager.domain.model.RouterSecurityContext
import com.immortalwrt.manager.domain.repository.CapabilityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CapabilityRepositoryImpl @Inject constructor(
    private val capabilityDao: RouterCapabilityDao,
    private val environmentDao: RouterEnvironmentDao,
    private val rpcClient: UbusJsonRpcClient
) : CapabilityRepository {

    override suspend fun getCapability(routerId: String): RouterCapability? {
        return withContext(Dispatchers.IO) {
            capabilityDao.getById(routerId)?.toDomain()
        }
    }

    override suspend fun probeCapability(
        routerId: String,
        securityContext: RouterSecurityContext,
        endpoint: String,
        sessionId: String
    ): AppResult<RouterCapability> = withContext(Dispatchers.IO) {
        val minResult = probeMinimumCapabilities(securityContext, endpoint, sessionId)
        if (minResult !is AppResult.Success) {
            return@withContext minResult as AppResult.Failure
        }

        val hasHostHints = rpcClient.call(
            securityContext, sessionId, endpoint, "luci-rpc", "getHostHints", emptyMap()
        ) is AppResult.Success

        val hasDhcpLeases = rpcClient.call(
            securityContext, sessionId, endpoint, "luci-rpc", "getDHCPLeases", mapOf("family" to 4)
        ) is AppResult.Success

        val hasTempInfo = rpcClient.call(
            securityContext, sessionId, endpoint, "luci", "getTempInfo", emptyMap()
        ) is AppResult.Success

        val hasNetworkDeviceStatus = rpcClient.call(
            securityContext, sessionId, endpoint, "network.device", "status", mapOf("name" to "br-lan")
        ) is AppResult.Success

        val canReboot = rpcClient.call(
            securityContext, sessionId, endpoint, "system", "reboot", emptyMap()
        ) is AppResult.Success

        val canChangePassword = rpcClient.call(
            securityContext, sessionId, endpoint, "luci", "setPassword", emptyMap()
        ) is AppResult.Success

        val hasLuciRpc = hasHostHints || hasDhcpLeases || hasTempInfo || canChangePassword

        val capability = RouterCapability(
            hasSessionLogin = true,
            hasSystemBoard = true,
            hasSystemInfo = true,
            hasNetworkInterfaceDump = true,
            hasNetworkInterfaceStatus = false,
            hasLuciRpc = hasLuciRpc,
            hasHostHints = hasHostHints,
            hasDhcpLeases = hasDhcpLeases,
            hasRealtimeStats = false,
            hasNetworkDeviceStatus = false,
            hasTempInfo = hasTempInfo,
            canReadUci = false,
            canReboot = canReboot,
            canChangePassword = canChangePassword
        )

        capabilityDao.upsert(capability.toEntity(routerId))
        AppResult.Success(capability)
    }

    override suspend fun getEnvironment(routerId: String): RouterEnvironment? {
        return withContext(Dispatchers.IO) {
            environmentDao.getById(routerId)?.toDomain()
        }
    }

    override suspend fun saveEnvironment(routerId: String, environment: RouterEnvironment) {
        withContext(Dispatchers.IO) {
            environmentDao.upsert(environment.toEntity(routerId))
        }
    }

    private suspend fun probeMinimumCapabilities(
        securityContext: RouterSecurityContext,
        endpoint: String,
        sessionId: String
    ): AppResult<Unit> = coroutineScope {
        val boardDeferred = async {
            rpcClient.call(securityContext, sessionId, endpoint, "system", "board", emptyMap())
        }
        val infoDeferred = async {
            rpcClient.call(securityContext, sessionId, endpoint, "system", "info", emptyMap())
        }
        val dumpDeferred = async {
            rpcClient.call(securityContext, sessionId, endpoint, "network.interface", "dump", emptyMap())
        }

        val boardResult = boardDeferred.await()
        val infoResult = infoDeferred.await()
        val dumpResult = dumpDeferred.await()

        val errors = mutableListOf<AppError>()
        if (boardResult is AppResult.Failure) errors.add(boardResult.error)
        if (infoResult is AppResult.Failure) errors.add(infoResult.error)
        if (dumpResult is AppResult.Failure) errors.add(dumpResult.error)

        if (errors.isNotEmpty()) {
            AppResult.Failure(errors.first())
        } else {
            AppResult.Success(Unit)
        }
    }
}

private fun RouterCapabilityEntity.toDomain(): RouterCapability = RouterCapability(
    hasSessionLogin = hasSessionLogin,
    hasSystemBoard = hasSystemBoard,
    hasSystemInfo = hasSystemInfo,
    hasNetworkInterfaceDump = hasNetworkInterfaceDump,
    hasNetworkInterfaceStatus = hasNetworkInterfaceStatus,
    hasLuciRpc = hasLuciRpc,
    hasHostHints = hasHostHints,
    hasDhcpLeases = hasDhcpLeases,
    hasRealtimeStats = hasRealtimeStats,
    hasNetworkDeviceStatus = hasNetworkDeviceStatus,
    hasTempInfo = hasTempInfo,
    canReadUci = canReadUci,
    canReboot = canReboot,
    canChangePassword = canChangePassword
)

private fun RouterCapability.toEntity(routerId: String): RouterCapabilityEntity = RouterCapabilityEntity(
    routerId = routerId,
    hasSessionLogin = hasSessionLogin,
    hasSystemBoard = hasSystemBoard,
    hasSystemInfo = hasSystemInfo,
    hasNetworkInterfaceDump = hasNetworkInterfaceDump,
    hasNetworkInterfaceStatus = hasNetworkInterfaceStatus,
    hasLuciRpc = hasLuciRpc,
    hasHostHints = hasHostHints,
    hasDhcpLeases = hasDhcpLeases,
    hasRealtimeStats = hasRealtimeStats,
    hasTempInfo = hasTempInfo,
    canReadUci = canReadUci,
    canReboot = canReboot,
    canChangePassword = canChangePassword
)

private fun RouterEnvironmentEntity.toDomain(): RouterEnvironment = RouterEnvironment(
    routerId = routerId,
    distribution = distribution,
    version = version,
    revision = revision,
    kernelVersion = kernelVersion,
    luciAvailable = luciAvailable,
    rpcdAvailable = rpcdAvailable,
    endpointMode = endpointMode ?: EndpointMode.DIRECT_UBUS
)

private fun RouterEnvironment.toEntity(routerId: String): RouterEnvironmentEntity = RouterEnvironmentEntity(
    routerId = routerId,
    distribution = distribution,
    version = version,
    revision = revision,
    kernelVersion = kernelVersion,
    luciAvailable = luciAvailable,
    rpcdAvailable = rpcdAvailable,
    endpointMode = endpointMode
)
