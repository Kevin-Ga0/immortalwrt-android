package com.immortalwrt.manager.core.network

import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CertificateFingerprintStore @Inject constructor() {
    private val fingerprints = ConcurrentHashMap<String, String>()

    fun getFingerprint(routerId: String): String? = fingerprints[routerId]

    fun storeFingerprint(routerId: String, fingerprint: String) {
        fingerprints[routerId] = fingerprint
    }

    fun removeFingerprint(routerId: String) {
        fingerprints.remove(routerId)
    }

    fun clearAll() {
        fingerprints.clear()
    }
}
