package com.immortalwrt.manager.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.immortalwrt.manager.domain.model.DeviceSource

@Entity(tableName = "device_caches")
data class DeviceCacheEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "router_id") val routerId: String,
    val mac: String?,
    val ip: String?,
    val hostname: String?,
    @ColumnInfo(name = "interface_name") val interfaceName: String?,
    val sources: Set<DeviceSource>,
    @ColumnInfo(name = "last_seen_at") val lastSeenAt: Long?,
    @ColumnInfo(name = "is_online") val isOnline: Boolean?,
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)
