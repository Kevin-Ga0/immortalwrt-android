package com.immortalwrt.manager.domain.model

data class DiagnosticReport(
    val routerName: String,
    val routerHost: String,
    val protocol: String,
    val endpointMode: String?,
    val connectionState: RouterConnectionState,
    val environment: RouterEnvironment?,
    val capability: RouterCapability?,
    val recentErrors: List<String>,
    val redactedSessionId: String?,
    val macAnonymized: Boolean,
    val ipAnonymized: Boolean,
    val hostnameHidden: Boolean
)
