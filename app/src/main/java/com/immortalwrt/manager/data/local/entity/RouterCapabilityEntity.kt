package com.immortalwrt.manager.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "router_capabilities")
data class RouterCapabilityEntity(
    @PrimaryKey @ColumnInfo(name = "router_id") val routerId: String,
    @ColumnInfo(name = "has_session_login") val hasSessionLogin: Boolean = false,
    @ColumnInfo(name = "has_system_board") val hasSystemBoard: Boolean = false,
    @ColumnInfo(name = "has_system_info") val hasSystemInfo: Boolean = false,
    @ColumnInfo(name = "has_network_interface_dump") val hasNetworkInterfaceDump: Boolean = false,
    @ColumnInfo(name = "has_network_interface_status") val hasNetworkInterfaceStatus: Boolean = false,
    @ColumnInfo(name = "has_network_device_status") val hasNetworkDeviceStatus: Boolean = false,
    @ColumnInfo(name = "has_luci_rpc") val hasLuciRpc: Boolean = false,
    @ColumnInfo(name = "has_host_hints") val hasHostHints: Boolean = false,
    @ColumnInfo(name = "has_dhcp_leases") val hasDhcpLeases: Boolean = false,
    @ColumnInfo(name = "has_realtime_stats") val hasRealtimeStats: Boolean = false,
    @ColumnInfo(name = "has_temp_info") val hasTempInfo: Boolean = false,
    @ColumnInfo(name = "can_read_uci") val canReadUci: Boolean = false,
    @ColumnInfo(name = "can_reboot") val canReboot: Boolean = false,
    @ColumnInfo(name = "can_change_password") val canChangePassword: Boolean = false
)
