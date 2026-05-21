package com.immortalwrt.manager.core.network

import com.immortalwrt.manager.domain.model.RouterScheme
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLException
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

@Singleton
class EndpointDiscovery @Inject constructor(
    private val networkMonitor: NetworkMonitor
) {
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    private val probeClient: OkHttpClient by lazy {
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun checkClientTrusted(
                    chain: Array<out X509Certificate>,
                    authType: String
                ) = Unit

                override fun checkServerTrusted(
                    chain: Array<out X509Certificate>,
                    authType: String
                ) = Unit

                override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
            }
        )
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCerts, SecureRandom())

        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .followRedirects(false)
            .followSslRedirects(false)
            .build()
    }

    companion object {
        private val CANDIDATE_ENDPOINTS = listOf(
            "/ubus",
            "/cgi-bin/luci/admin/ubus",
            "/admin/ubus"
        )

        private val CAPABILITY_PROBES = listOf(
            Triple("system", "board", "system.board"),
            Triple("system", "info", "system.info"),
            Triple("network.interface", "dump", "network.interface.dump")
        )
    }

    suspend fun probeEndpoint(
        host: String,
        port: Int,
        scheme: RouterScheme
    ): EndpointProbeResult {
        if (!networkMonitor.isNetworkAvailable()) {
            return EndpointProbeResult(
                layer1Connectivity = Layer1Result.Unreachable("NO_NETWORK"),
                layer2Endpoints = emptyMap(),
                layer3Auth = null
            )
        }

        val layer1 = checkLayer1Connectivity(host, port, scheme)
        if (layer1 is Layer1Result.Unreachable) {
            return EndpointProbeResult(
                layer1Connectivity = layer1,
                layer2Endpoints = emptyMap(),
                layer3Auth = null
            )
        }

        val layer2 = probeLayer2Endpoints(host, port, scheme)

        return EndpointProbeResult(
            layer1Connectivity = layer1,
            layer2Endpoints = layer2,
            layer3Auth = null
        )
    }

    suspend fun probeAuthAndCapability(
        host: String,
        port: Int,
        scheme: RouterScheme,
        endpoint: String,
        username: String,
        password: String
    ): Layer3Result {
        if (!networkMonitor.isNetworkAvailable()) {
            return Layer3Result.AuthFailed("NO_NETWORK")
        }

        val loginResult = attemptLogin(host, port, scheme, endpoint, username, password)
        if (loginResult is Layer3Result.AuthFailed || loginResult is Layer3Result.MinCapabilityFailed) {
            return loginResult
        }

        val sessionId = (loginResult as? Layer3Result.Success)?.sessionId
            ?: return Layer3Result.AuthFailed("BAD_CREDENTIALS")

        val missingCapabilities = mutableListOf<String>()
        for ((obj, method, label) in CAPABILITY_PROBES) {
            val available = checkCapability(host, port, scheme, endpoint, sessionId, obj, method)
            if (!available) {
                missingCapabilities.add(label)
            }
        }

        return if (missingCapabilities.isEmpty()) {
            Layer3Result.Success(sessionId)
        } else {
            Layer3Result.MinCapabilityFailed(missingCapabilities.toList())
        }
    }

    private suspend fun checkLayer1Connectivity(
        host: String,
        port: Int,
        scheme: RouterScheme
    ): Layer1Result {
        val url = buildUrl(scheme, host, port, "/")
        val request = Request.Builder()
            .url(url)
            .head()
            .build()

        return try {
            val response = probeClient.newCall(request).execute()
            response.close()
            Layer1Result.Reachable
        } catch (e: UnknownHostException) {
            Layer1Result.Unreachable("DNS_FAILED")
        } catch (e: SSLException) {
            Layer1Result.Unreachable("TLS_FAILED")
        } catch (e: SocketTimeoutException) {
            Layer1Result.Unreachable("TIMEOUT")
        } catch (e: ConnectException) {
            Layer1Result.Unreachable("TIMEOUT")
        } catch (e: Exception) {
            val message = e.message ?: "UNKNOWN"
            Layer1Result.Unreachable(message)
        }
    }

    private suspend fun probeLayer2Endpoints(
        host: String,
        port: Int,
        scheme: RouterScheme
    ): Map<String, Layer2Result> {
        val results = mutableMapOf<String, Layer2Result>()
        val dummyPayload = buildUbusLoginPayload(
            sessionId = "00000000000000000000000000000000",
            username = "test",
            password = "test"
        )

        for (endpoint in CANDIDATE_ENDPOINTS) {
            results[endpoint] = probeSingleEndpoint(host, port, scheme, endpoint, dummyPayload)
        }

        return results
    }

    private suspend fun probeSingleEndpoint(
        host: String,
        port: Int,
        scheme: RouterScheme,
        endpoint: String,
        jsonPayload: String
    ): Layer2Result {
        val url = buildUrl(scheme, host, port, endpoint)
        val body = jsonPayload.toRequestBody(jsonMediaType)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Content-Type", "application/json")
            .build()

        return try {
            val response = probeClient.newCall(request).execute()
            val bodyString = response.body?.string() ?: ""

            if (response.code == 404 || bodyString.startsWith("<")) {
                Layer2Result.Unavailable(response.code, bodyString.take(200))
            } else if (bodyString.startsWith("{")) {
                Layer2Result.Available
            } else {
                Layer2Result.Unavailable(response.code, bodyString.take(200))
            }
        } catch (e: Exception) {
            Layer2Result.Error(e.message ?: "Probe failed for endpoint $endpoint")
        }
    }

    private suspend fun attemptLogin(
        host: String,
        port: Int,
        scheme: RouterScheme,
        endpoint: String,
        username: String,
        password: String
    ): Layer3Result {
        val payload = buildUbusLoginPayload(
            sessionId = "00000000000000000000000000000000",
            username = username,
            password = password
        )

        val url = buildUrl(scheme, host, port, endpoint)
        val body = payload.toRequestBody(jsonMediaType)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Content-Type", "application/json")
            .build()

        return try {
            val response = probeClient.newCall(request).execute()
            val bodyString = response.body?.string() ?: ""

            if (!bodyString.startsWith("{")) {
                return Layer3Result.AuthFailed("BAD_CREDENTIALS")
            }

            val json = JSONObject(bodyString)

            if (json.has("error")) {
                val errorObj = json.getJSONObject("error")
                val code = errorObj.optInt("code", -1)
                val message = errorObj.optString("message", "")
                return if (code == -32002 || message.contains("permission", ignoreCase = true)) {
                    Layer3Result.AuthFailed("PERMISSION_DENIED")
                } else {
                    Layer3Result.AuthFailed("BAD_CREDENTIALS")
                }
            }

            if (!json.has("result")) {
                return Layer3Result.AuthFailed("BAD_CREDENTIALS")
            }

            val resultArray = json.getJSONArray("result")
            val ubusCode = resultArray.optInt(0, -1)

            when (ubusCode) {
                0 -> {
                    val data = resultArray.optJSONObject(1)
                    val sessionId = data?.optString("ubus_rpc_session", null as String?)
                    if (sessionId != null) {
                        Layer3Result.Success(sessionId)
                    } else {
                        Layer3Result.AuthFailed("BAD_CREDENTIALS")
                    }
                }
                6 -> Layer3Result.AuthFailed("PERMISSION_DENIED")
                else -> Layer3Result.AuthFailed("BAD_CREDENTIALS")
            }
        } catch (e: Exception) {
            Layer3Result.AuthFailed("BAD_CREDENTIALS")
        }
    }

    private suspend fun checkCapability(
        host: String,
        port: Int,
        scheme: RouterScheme,
        endpoint: String,
        sessionId: String,
        ubusObject: String,
        ubusMethod: String
    ): Boolean {
        val payload = buildUbusCallPayload(sessionId, ubusObject, ubusMethod, "{}")

        val url = buildUrl(scheme, host, port, endpoint)
        val body = payload.toRequestBody(jsonMediaType)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Content-Type", "application/json")
            .build()

        return try {
            val response = probeClient.newCall(request).execute()
            val bodyString = response.body?.string() ?: ""

            if (!bodyString.startsWith("{")) return false

            val json = JSONObject(bodyString)
            if (!json.has("result")) return false

            val resultArray = json.getJSONArray("result")
            val ubusCode = resultArray.optInt(0, -1)
            ubusCode == 0
        } catch (e: Exception) {
            false
        }
    }

    private fun buildUrl(scheme: RouterScheme, host: String, port: Int, path: String): String {
        return "${scheme.name.lowercase()}://$host:$port$path"
    }

    private fun buildUbusLoginPayload(sessionId: String, username: String, password: String): String {
        val params = JSONObject().apply {
            put("username", username)
            put("password", password)
            put("timeout", 300)
        }
        val callParams = JSONArray().apply {
            put(sessionId)
            put("session")
            put("login")
            put(params)
        }
        return JSONObject().apply {
            put("jsonrpc", "2.0")
            put("id", 1)
            put("method", "call")
            put("params", callParams)
        }.toString()
    }

    private fun buildUbusCallPayload(
        sessionId: String,
        ubusObject: String,
        ubusMethod: String,
        paramsJson: String
    ): String {
        val params = JSONObject(paramsJson)
        val callParams = JSONArray().apply {
            put(sessionId)
            put(ubusObject)
            put(ubusMethod)
            put(params)
        }
        return JSONObject().apply {
            put("jsonrpc", "2.0")
            put("id", 1)
            put("method", "call")
            put("params", callParams)
        }.toString()
    }
}
