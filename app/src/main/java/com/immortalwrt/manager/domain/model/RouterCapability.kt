package com.immortalwrt.manager.domain.model

data class RouterCapability(
    val hasSessionLogin: Boolean,
    val hasSystemBoard: Boolean,
    val hasSystemInfo: Boolean,
    val hasNetworkInterfaceDump: Boolean,
    val hasNetworkInterfaceStatus: Boolean,
    val hasNetworkDeviceStatus: Boolean,
    val hasLuciRpc: Boolean,
    val hasHostHints: Boolean,
    val hasDhcpLeases: Boolean,
    val hasRealtimeStats: Boolean,
    val hasTempInfo: Boolean,
    val canReadUci: Boolean,
    val canReboot: Boolean,
    val canChangePassword: Boolean
)
