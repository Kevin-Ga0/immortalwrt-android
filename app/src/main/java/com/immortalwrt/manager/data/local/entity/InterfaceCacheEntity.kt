package com.immortalwrt.manager.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "interface_caches")
data class InterfaceCacheEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "router_id") val routerId: String,
    @ColumnInfo(name = "logical_name") val logicalName: String,
    @ColumnInfo(name = "device_name") val deviceName: String?,
    @ColumnInfo(name = "display_name") val displayName: String,
    val up: Boolean = false,
    val mac: String?,
    @ColumnInfo(name = "ip_addrs") val ipAddrs: List<String>,
    @ColumnInfo(name = "ip6_addrs") val ip6Addrs: List<String>,
    @ColumnInfo(name = "rx_bytes") val rxBytes: Long = 0,
    @ColumnInfo(name = "tx_bytes") val txBytes: Long = 0,
    @ColumnInfo(name = "rx_packets") val rxPackets: Long = 0,
    @ColumnInfo(name = "tx_packets") val txPackets: Long = 0,
    val mtu: Int?,
    @ColumnInfo(name = "dev_type") val devType: String?,
    val dns: List<String>,
    val gateway: String?,
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)
