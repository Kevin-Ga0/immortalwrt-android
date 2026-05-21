package com.immortalwrt.manager.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.immortalwrt.manager.domain.model.EndpointMode

@Entity(tableName = "router_environments")
data class RouterEnvironmentEntity(
    @PrimaryKey @ColumnInfo(name = "router_id") val routerId: String,
    val distribution: String?,
    val version: String?,
    val revision: String?,
    @ColumnInfo(name = "kernel_version") val kernelVersion: String?,
    @ColumnInfo(name = "luci_available") val luciAvailable: Boolean?,
    @ColumnInfo(name = "rpcd_available") val rpcdAvailable: Boolean?,
    @ColumnInfo(name = "endpoint_mode") val endpointMode: EndpointMode?
)
