package com.immortalwrt.manager.data.repository

import com.immortalwrt.manager.core.network.UbusJsonRpcClient
import com.immortalwrt.manager.data.local.dao.RouterCacheDao
import com.immortalwrt.manager.data.local.entity.DeviceCacheEntity
import com.immortalwrt.manager.domain.model.AppResult
import com.immortalwrt.manager.domain.model.ConnectedDevice
import com.immortalwrt.manager.domain.model.DeviceSource
import com.immortalwrt.manager.domain.model.RouterSecurityContext
import com.immortalwrt.manager.domain.repository.DeviceRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepositoryImpl @Inject constructor(
    private val rpcClient: UbusJsonRpcClient,
    private val routerCacheDao: RouterCacheDao
) : DeviceRepository {

    override suspend fun getDevices(routerId: String): AppResult<List<ConnectedDevice>> {
        val cached = routerCacheDao.getDeviceCaches(routerId)
        return AppResult.Success(cached.map { it.toDomain() })
    }

    override suspend fun getCachedDevices(routerId: String): List<ConnectedDevice> {
        return routerCacheDao.getDeviceCaches(routerId).map { it.toDomain() }
    }

    override suspend fun refreshDevices(
        routerId: String,
        securityContext: RouterSecurityContext,
        endpoint: String,
        sessionId: String
    ): AppResult<List<ConnectedDevice>> = coroutineScope {
        val dhcpDeferred = async {
            rpcClient.call(
                securityContext, sessionId, endpoint,
                "luci-rpc", "getDHCPLeases", mapOf("family" to 4)
            )
        }
        val hintsDeferred = async {
            rpcClient.call(
                securityContext, sessionId, endpoint,
                "luci-rpc", "getHostHints", emptyMap()
            )
        }

        val dhcpResult = dhcpDeferred.await()
        val hintsResult = hintsDeferred.await()

        // Graceful degradation: if both APIs fail, propagate error
        if (dhcpResult is AppResult.Failure && hintsResult is AppResult.Failure) {
            return@coroutineScope dhcpResult
        }

        val dhcpDevices = parseDhcpLeases(dhcpResult)
        val hintDevices = parseHostHints(hintsResult)

        // Build MAC and IP indices for deduplication
        val macIndex = mutableMapOf<String, MutableList<ConnectedDevice>>()
        val ipIndex = mutableMapOf<String, MutableList<ConnectedDevice>>()

        for (device in (dhcpDevices + hintDevices)) {
            val normMac = normalizeMac(device.mac)
            if (normMac != null) {
                macIndex.getOrPut(normMac) { mutableListOf() }.add(device)
            }
            if (device.ip != null) {
                ipIndex.getOrPut(device.ip) { mutableListOf() }.add(device)
            }
        }

        // Merge groups by normalized MAC (primary dedup key)
        val merged = mutableMapOf<String, ConnectedDevice>()
        val mergedIpToMac = mutableMapOf<String, String>()

        for ((normMac, devices) in macIndex) {
            val mergedDevice = devices.reduce { acc, dev -> mergeDevices(acc, dev) }
            merged[normMac] = mergedDevice
            mergedDevice.ip?.let { mergedIpToMac[it] = normMac }
        }

        // Handle remaining MAC-less devices: match by IP, or add as new
        for (device in (dhcpDevices + hintDevices)) {
            val normMac = normalizeMac(device.mac)
            if (normMac != null) continue // Already merged via MAC

            if (device.ip != null && mergedIpToMac.containsKey(device.ip)) {
                // IP matches an existing device — merge sources
                val targetMac = mergedIpToMac[device.ip]!!
                merged[targetMac] = mergeDevices(merged[targetMac]!!, device)
            } else if (device.ip != null) {
                // New device identified only by IP
                val ipKey = "_ip_${device.ip}"
                merged[ipKey] = device
            }
        }

        // Persist to cache
        val entities = merged.values.map { device -> device.toEntity(routerId) }
        routerCacheDao.upsertDevices(entities)

        // Return merged devices sorted by IP address
        val sortedDevices = merged.values.toList().sortedBy { it.ip }
        AppResult.Success(sortedDevices)
    }

    // ------------------------------------------------------------------
    // Private parsing helpers
    // ------------------------------------------------------------------

    private fun parseDhcpLeases(result: AppResult<Any?>): List<ConnectedDevice> {
        if (result !is AppResult.Success) return emptyList()
        val data = result.data as? Map<*, *> ?: return emptyList()
        val leases = data["dhcp_leases"] as? List<Map<*, *>> ?: return emptyList()
        val now = System.currentTimeMillis()
        return leases.map { lease ->
            ConnectedDevice(
                ip = lease["ipaddr"] as? String,
                mac = lease["macaddr"] as? String,
                hostname = lease["hostname"] as? String,
                interfaceName = null,
                sources = setOf(DeviceSource.DHCP_LEASE),
                lastSeenAt = now,
                isOnline = ((lease["expires"] as? Number)?.toInt() ?: 0) > 0
            )
        }
    }

    private fun parseHostHints(result: AppResult<Any?>): List<ConnectedDevice> {
        if (result !is AppResult.Success) return emptyList()
        val data = result.data as? Map<*, *> ?: return emptyList()
        val now = System.currentTimeMillis()
        return data.entries.mapNotNull { (mac, info) ->
            val hint = info as? Map<*, *> ?: return@mapNotNull null
            val ipAddrs = hint["ipaddrs"] as? List<*> ?: emptyList<Any>()
            ConnectedDevice(
                ip = ipAddrs.firstOrNull() as? String,
                mac = mac as? String,
                hostname = hint["name"] as? String,
                interfaceName = null,
                sources = setOf(DeviceSource.HOST_HINT),
                lastSeenAt = now,
                isOnline = true
            )
        }
    }

    // ------------------------------------------------------------------
    // Merge / mapping utilities
    // ------------------------------------------------------------------

    private fun mergeDevices(a: ConnectedDevice, b: ConnectedDevice): ConnectedDevice {
        return ConnectedDevice(
            ip = a.ip ?: b.ip,
            mac = a.mac ?: b.mac,
            hostname = a.hostname ?: b.hostname,
            interfaceName = a.interfaceName ?: b.interfaceName,
            sources = a.sources + b.sources,
            lastSeenAt = maxOf(a.lastSeenAt ?: 0L, b.lastSeenAt ?: 0L)
                .let { if (it == 0L) null else it },
            isOnline = a.isOnline == true || b.isOnline == true
        )
    }

    private fun ConnectedDevice.toEntity(routerId: String): DeviceCacheEntity {
        val normMac = normalizeMac(mac)
        val id = routerId + "_" + (normMac ?: ip ?: "anon_${System.identityHashCode(this)}")
        return DeviceCacheEntity(
            id = id,
            routerId = routerId,
            mac = mac,
            ip = ip,
            hostname = hostname,
            interfaceName = interfaceName,
            sources = sources,
            lastSeenAt = lastSeenAt,
            isOnline = isOnline,
            updatedAt = System.currentTimeMillis()
        )
    }

    private fun DeviceCacheEntity.toDomain(): ConnectedDevice {
        return ConnectedDevice(
            ip = ip,
            mac = mac,
            hostname = hostname,
            interfaceName = interfaceName,
            sources = sources,
            lastSeenAt = lastSeenAt,
            isOnline = isOnline
        )
    }

    // ------------------------------------------------------------------
    // MAC address normalization
    // ------------------------------------------------------------------

    private fun normalizeMac(mac: String?): String? {
        if (mac == null) return null
        val hex = mac.uppercase()
            .replace(Regex("[:-]"), "")
            .replace(".", "")
        if (hex.length != 12 || hex.all { it == '0' } || hex == "FFFFFFFFFFFF") return null
        val firstByte = hex.substring(0, 2).toIntOrNull(16) ?: return null
        // Reject multicast MACs (least significant bit of first octet)
        if ((firstByte and 0x01) != 0) return null
        return hex.chunked(2).joinToString(":")
    }
}
