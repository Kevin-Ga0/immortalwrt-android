package com.immortalwrt.manager.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "system_snapshots")
data class SystemSnapshotEntity(
    @PrimaryKey @ColumnInfo(name = "router_id") val routerId: String,
    val hostname: String?,
    val model: String?,
    val architecture: String?,
    @ColumnInfo(name = "target_platform") val targetPlatform: String?,
    @ColumnInfo(name = "firmware_version") val firmwareVersion: String?,
    @ColumnInfo(name = "kernel_version") val kernelVersion: String?,
    @ColumnInfo(name = "local_time") val localTime: Long?,
    val uptime: Long?,
    @ColumnInfo(name = "load_average") val loadAverage: List<Float>,
    @ColumnInfo(name = "memory_total") val memoryTotal: Long = 0,
    @ColumnInfo(name = "memory_free") val memoryFree: Long = 0,
    @ColumnInfo(name = "memory_buffered") val memoryBuffered: Long = 0,
    @ColumnInfo(name = "swap_total") val swapTotal: Long = 0,
    @ColumnInfo(name = "swap_free") val swapFree: Long = 0,
    val temperature: String?,
    @ColumnInfo(name = "cpu_usage") val cpuUsage: String?,
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)
