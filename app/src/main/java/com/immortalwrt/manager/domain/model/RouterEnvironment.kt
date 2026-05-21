package com.immortalwrt.manager.domain.model

enum class EndpointMode { DIRECT_UBUS, LUCI_PROXY, CUSTOM }

data class RouterEnvironment(
    val routerId: String? = null,
    val distribution: String? = null,
    val version: String? = null,
    val revision: String? = null,
    val kernelVersion: String? = null,
    val luciAvailable: Boolean? = null,
    val rpcdAvailable: Boolean? = null,
    val endpointMode: EndpointMode
)
