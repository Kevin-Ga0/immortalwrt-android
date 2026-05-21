package com.immortalwrt.manager.domain.model

enum class DeviceSource { DHCP_LEASE, HOST_HINT, INTERFACE_NEIGHBOR, CACHE }

data class ConnectedDevice(
    val ip: String? = null,
    val mac: String? = null,
    val hostname: String? = null,
    val interfaceName: String? = null,
    val sources: Set<DeviceSource> = emptySet(),
    val lastSeenAt: Long? = null,
    val isOnline: Boolean? = null
)

data class DeviceAddress(
    val address: String,
    val family: Int,
    val source: DeviceSource
)
