package com.immortalwrt.manager.domain.model

data class NetworkInterface(
    val logicalName: String,
    val deviceName: String? = null,
    val displayName: String,
    val up: Boolean = false,
    val mac: String? = null,
    val ipAddrs: List<String> = emptyList(),
    val ip6Addrs: List<String> = emptyList(),
    val rxBytes: Long = 0,
    val txBytes: Long = 0,
    val rxPackets: Long = 0,
    val txPackets: Long = 0,
    val mtu: Int? = null,
    val devType: String? = null,
    val dns: List<String> = emptyList(),
    val gateway: String? = null
)
