package com.immortalwrt.manager.core.network

import com.immortalwrt.manager.BuildConfig
import com.immortalwrt.manager.core.util.DebugLogger
import com.immortalwrt.manager.domain.model.RouterScheme
import com.immortalwrt.manager.domain.model.RouterSecurityContext
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Singleton
class RouterOkHttpClientFactory @Inject constructor(
    private val certificateTrustManager: CertificateTrustManager,
    private val fingerprintStore: CertificateFingerprintStore
) {
    private val clientCache = ConcurrentHashMap<String, OkHttpClient>()

    fun createClient(securityContext: RouterSecurityContext): OkHttpClient {
        return clientCache.getOrPut(securityContext.routerId) {
            createNewClient(securityContext)
        }
    }

    fun invalidateClient(routerId: String) {
        clientCache.remove(routerId)?.let { client ->
            client.dispatcher.executorService.shutdown()
            client.connectionPool.evictAll()
        }
    }

    fun clearAll() {
        clientCache.keys.toList().forEach { invalidateClient(it) }
    }

    private fun createNewClient(securityContext: RouterSecurityContext): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor { message ->
                val sanitized = message.replace(
                    Regex("(Authorization|Cookie|Set-Cookie|X-API-Key): .*", RegexOption.IGNORE_CASE)
                ) { "${it.groupValues[1]}: [REDACTED]" }
                DebugLogger.d("OkHttp", sanitized)
            }
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }

        if (securityContext.scheme == RouterScheme.HTTPS) {
            configureTls(builder, securityContext)
        }

        return builder.build()
    }

    private fun configureTls(
        builder: OkHttpClient.Builder,
        securityContext: RouterSecurityContext
    ) {
        val trustManager = object : X509TrustManager {
            override fun checkClientTrusted(
                chain: Array<out X509Certificate>,
                authType: String
            ) = Unit

            override fun checkServerTrusted(
                chain: Array<out X509Certificate>,
                authType: String
            ) {
                val cert = chain.firstOrNull()
                    ?: throw CertificateException("No server certificate in chain")

                val fingerprint = certificateTrustManager.computeFingerprint(cert)
                val stored = fingerprintStore.getFingerprint(securityContext.routerId)

                when {
                    stored == null -> {
                        fingerprintStore.storeFingerprint(securityContext.routerId, fingerprint)
                    }
                    stored != fingerprint -> {
                        throw CertificateException(
                            "Certificate fingerprint changed for router ${securityContext.routerId}"
                        )
                    }
                }
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
        }

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(trustManager), null)
        builder.sslSocketFactory(sslContext.socketFactory, trustManager)
        builder.hostnameVerifier { _, _ -> true }
    }
}
