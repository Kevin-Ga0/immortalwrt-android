package com.immortalwrt.manager.core.network

import com.immortalwrt.manager.domain.model.AppError
import com.immortalwrt.manager.domain.model.AppResult
import com.immortalwrt.manager.domain.model.RouterSecurityContext
import java.security.MessageDigest
import java.security.cert.X509Certificate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CertificateTrustManager @Inject constructor(
    private val fingerprintStore: CertificateFingerprintStore
) {
    fun computeFingerprint(cert: X509Certificate): String {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(cert.encoded)
            .joinToString("") { "%02X".format(it) }
            .chunked(2)
            .joinToString(":")
    }

    suspend fun verifyCertificate(
        securityContext: RouterSecurityContext,
        certificate: X509Certificate
    ): AppResult<Unit> {
        val fingerprint = computeFingerprint(certificate)
        val stored = fingerprintStore.getFingerprint(securityContext.routerId)

        return when {
            stored == null -> AppResult.Failure(AppError.CertificateUntrusted(fingerprint))
            stored == fingerprint -> AppResult.Success(Unit)
            else -> AppResult.Failure(AppError.CertificateChanged(stored, fingerprint))
        }
    }
}
