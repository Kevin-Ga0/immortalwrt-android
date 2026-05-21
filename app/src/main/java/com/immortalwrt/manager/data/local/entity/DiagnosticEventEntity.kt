package com.immortalwrt.manager.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diagnostic_events")
data class DiagnosticEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "router_id") val routerId: String,
    val timestamp: Long,
    val layer: String,  // "CONNECTIVITY", "ENDPOINT", "AUTH"
    val status: String,
    val message: String
)
