package com.immortalwrt.manager.core.network

data class EndpointProbeResult(
    val layer1Connectivity: Layer1Result,
    val layer2Endpoints: Map<String, Layer2Result>,
    val layer3Auth: Layer3Result?
)

sealed interface Layer1Result {
    data object Reachable : Layer1Result
    data class Unreachable(val reason: String) : Layer1Result
}

sealed interface Layer2Result {
    data object Available : Layer2Result
    data class Unavailable(val httpStatus: Int, val bodyPreview: String?) : Layer2Result
    data class Error(val message: String) : Layer2Result
}

sealed interface Layer3Result {
    data class Success(val sessionId: String) : Layer3Result
    data class AuthFailed(val reason: String) : Layer3Result
    data class MinCapabilityFailed(val missing: List<String>) : Layer3Result
    data object NotAttempted : Layer3Result
}
