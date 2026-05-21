package com.immortalwrt.manager.domain.model

sealed interface AppError {
    data object NetworkUnavailable : AppError
    data object Timeout : AppError
    data object DnsFailed : AppError
    data object EndpointNotFound : AppError
    data object AuthFailed : AppError
    data object PermissionDenied : AppError
    data object SessionExpired : AppError
    data class CertificateUntrusted(val fingerprint: String) : AppError
    data class CertificateChanged(val storedFingerprint: String, val currentFingerprint: String) : AppError
    data class UbusError(val code: Int, val message: String?) : AppError
    data class JsonParseError(val rawSample: String?) : AppError
    data class UnsupportedCapability(val capability: String) : AppError
    data class HttpError(val statusCode: Int, val bodySample: String?) : AppError
    data class Unknown(val throwable: Throwable?) : AppError
}
