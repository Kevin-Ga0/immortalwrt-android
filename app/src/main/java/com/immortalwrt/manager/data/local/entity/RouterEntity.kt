package com.immortalwrt.manager.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.immortalwrt.manager.domain.model.RouterScheme

@Entity(tableName = "routers")
data class RouterEntity(
    @PrimaryKey val id: String,
    val name: String,
    val host: String,
    val port: Int,
    val scheme: RouterScheme,
    val endpoint: String?,
    val username: String,
    val passwordSecretRef: String,
    val certificateFingerprint: String?,
    @ColumnInfo(name = "http_risk_accepted") val httpRiskAccepted: Boolean = false,
    @ColumnInfo(name = "last_success_at") val lastSuccessAt: Long?,
    @ColumnInfo(name = "last_failure_reason") val lastFailureReason: String?,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)
