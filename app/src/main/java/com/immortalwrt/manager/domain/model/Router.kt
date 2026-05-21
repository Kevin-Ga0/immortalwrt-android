package com.immortalwrt.manager.domain.model

enum class RouterScheme { HTTP, HTTPS }

data class Router(
    val id: String,
    val name: String,
    val host: String,
    val port: Int,
    val scheme: RouterScheme,
    val endpoint: String? = null,
    val username: String,
    val passwordSecretRef: String? = null,
    val certificateFingerprint: String? = null,
    val lastSuccessAt: Long? = null,
    val lastFailureReason: String? = null,
    val createdAt: Long,
    val updatedAt: Long
)

data class RouterSession(
    val routerId: String,
    val sessionId: String,
    val timeoutSeconds: Int? = null,
    val expiresAtEpochMillis: Long? = null,
    val createdAtEpochMillis: Long
)

data class RouterSecurityContext(
    val routerId: String,
    val scheme: RouterScheme,
    val host: String,
    val port: Int,
    val certificateFingerprint: String? = null,
    val httpRiskAccepted: Boolean
)
