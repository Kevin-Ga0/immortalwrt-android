package com.immortalwrt.manager.data.repository

import com.immortalwrt.manager.data.local.dao.RouterCacheDao
import com.immortalwrt.manager.data.local.entity.InterfaceCacheEntity
import com.immortalwrt.manager.data.remote.NetworkRemoteDataSource
import com.immortalwrt.manager.domain.model.AppError
import com.immortalwrt.manager.domain.model.AppResult
import com.immortalwrt.manager.domain.model.NetworkInterface
import com.immortalwrt.manager.domain.model.RouterSecurityContext
import com.immortalwrt.manager.domain.repository.NetworkRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepositoryImpl @Inject constructor(
    private val remoteDataSource: NetworkRemoteDataSource,
    private val dao: RouterCacheDao
) : NetworkRepository {

    override suspend fun getInterfaces(routerId: String): AppResult<List<NetworkInterface>> {
        val cached = dao.getInterfaceCaches(routerId)
        if (cached.isEmpty()) {
            return AppResult.Success(emptyList())
        }
        return AppResult.Success(cached.map { it.toDomain() })
    }

    override suspend fun getCachedInterfaces(routerId: String): List<NetworkInterface> {
        return dao.getInterfaceCaches(routerId).map { it.toDomain() }
    }

    override suspend fun refreshInterfaces(
        routerId: String,
        securityContext: RouterSecurityContext,
        endpoint: String,
        sessionId: String
    ): AppResult<List<NetworkInterface>> {
        val dumpResult = remoteDataSource.fetchInterfaceDump(
            securityContext, endpoint, sessionId
        )

        val rawDump = when (dumpResult) {
            is AppResult.Success -> dumpResult.data
            is AppResult.Failure -> return dumpResult
        }

        val dumpList = rawDump as? List<*>
        if (dumpList == null) {
            return AppResult.Failure(AppError.Unknown(null))
        }

        val interfaceNames = mutableListOf<String>()
        for (item in dumpList) {
            val map = item as? Map<*, *>
            val name = map?.get("interface") as? String
            if (name != null) {
                interfaceNames.add(name)
            }
        }

        val networkInterfaces = mutableListOf<NetworkInterface>()
        for (name in interfaceNames) {
            val statusResult = remoteDataSource.fetchInterfaceStatus(
                securityContext, endpoint, sessionId, name
            )
            when (statusResult) {
                is AppResult.Success -> {
                    val statusData = statusResult.data as? Map<String, Any?>
                    if (statusData != null) {
                        networkInterfaces.add(parseInterfaceStatus(name, statusData))
                    }
                }
                is AppResult.Failure -> {
                    // Skip interfaces whose status could not be fetched
                }
            }
        }

        val entities = networkInterfaces.map { it.toEntity(routerId) }
        dao.upsertInterfaces(entities)

        return AppResult.Success(networkInterfaces)
    }

    private fun parseInterfaceStatus(
        logicalName: String,
        data: Map<String, Any?>
    ): NetworkInterface {
        val stats = data["statistics"] as? Map<String, Any?>
        val routes = data["route"] as? List<Map<String, Any?>>
        val ipv4Addresses = data["ipv4-address"] as? List<Map<String, Any?>>
        val ipv6Addresses = data["ipv6-address"] as? List<Map<String, Any?>>
        val dnsServers = data["dns-server"] as? List<String>

        return NetworkInterface(
            logicalName = logicalName,
            deviceName = data["device"] as? String,
            displayName = (data["device"] as? String)
                ?.let { dev -> "$logicalName ($dev)" } ?: logicalName,
            up = (data["up"] as? Boolean) ?: false,
            mac = data["macaddr"] as? String,
            ipAddrs = ipv4Addresses
                ?.mapNotNull { it["address"] as? String } ?: emptyList(),
            ip6Addrs = ipv6Addresses
                ?.mapNotNull { it["address"] as? String } ?: emptyList(),
            rxBytes = (stats?.get("rx_bytes") as? Number)?.toLong() ?: 0,
            txBytes = (stats?.get("tx_bytes") as? Number)?.toLong() ?: 0,
            rxPackets = (stats?.get("rx_packets") as? Number)?.toLong() ?: 0,
            txPackets = (stats?.get("tx_packets") as? Number)?.toLong() ?: 0,
            mtu = (data["mtu"] as? Number)?.toInt(),
            devType = data["type"] as? String,
            dns = dnsServers ?: emptyList(),
            gateway = routes
                ?.find { it["target"] == "0.0.0.0" }
                ?.get("gateway") as? String
        )
    }

    private fun InterfaceCacheEntity.toDomain(): NetworkInterface {
        return NetworkInterface(
            logicalName = logicalName,
            deviceName = deviceName,
            displayName = displayName,
            up = up,
            mac = mac,
            ipAddrs = ipAddrs,
            ip6Addrs = ip6Addrs,
            rxBytes = rxBytes,
            txBytes = txBytes,
            rxPackets = rxPackets,
            txPackets = txPackets,
            mtu = mtu,
            devType = devType,
            dns = dns,
            gateway = gateway
        )
    }

    private fun NetworkInterface.toEntity(routerId: String): InterfaceCacheEntity {
        return InterfaceCacheEntity(
            id = "${routerId}_$logicalName",
            routerId = routerId,
            logicalName = logicalName,
            deviceName = deviceName,
            displayName = displayName,
            up = up,
            mac = mac,
            ipAddrs = ipAddrs,
            ip6Addrs = ip6Addrs,
            rxBytes = rxBytes,
            txBytes = txBytes,
            rxPackets = rxPackets,
            txPackets = txPackets,
            mtu = mtu,
            devType = devType,
            dns = dns,
            gateway = gateway,
            updatedAt = System.currentTimeMillis()
        )
    }
}
