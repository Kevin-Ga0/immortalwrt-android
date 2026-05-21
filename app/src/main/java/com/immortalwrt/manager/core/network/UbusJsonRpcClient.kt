package com.immortalwrt.manager.core.network

import com.immortalwrt.manager.core.security.SensitiveLogSanitizer
import com.immortalwrt.manager.core.util.DebugLogger
import com.immortalwrt.manager.domain.model.AppError
import com.immortalwrt.manager.domain.model.AppResult
import com.immortalwrt.manager.domain.model.RouterSecurityContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UbusJsonRpcClient @Inject constructor(
    private val clientFactory: RouterOkHttpClientFactory,
    private val idGenerator: UbusCallIdGenerator
) {
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun call(
        securityContext: RouterSecurityContext,
        sessionId: String?,
        endpoint: String,
        ubusObject: String,
        ubusMethod: String,
        params: Map<String, Any?> = emptyMap()
    ): AppResult<Any?> = withContext(Dispatchers.IO) {
        try {
            parseUbusResult(executeRequest(securityContext, sessionId, endpoint, ubusObject, ubusMethod, params))
        } catch (e: SocketTimeoutException) {
            DebugLogger.e("UbusJsonRpcClient", "Timeout", e)
            AppResult.Failure(AppError.Timeout)
        } catch (e: IOException) {
            DebugLogger.e("UbusJsonRpcClient", "Network error", e)
            AppResult.Failure(AppError.NetworkUnavailable)
        } catch (e: org.json.JSONException) {
            DebugLogger.e("UbusJsonRpcClient", "JSON parse error", e)
            AppResult.Failure(AppError.JsonParseError(e.message?.take(200)))
        } catch (e: Exception) {
            DebugLogger.e("UbusJsonRpcClient", "Unexpected error", e)
            AppResult.Failure(AppError.Unknown(e))
        }
    }

    private fun executeRequest(
        securityContext: RouterSecurityContext,
        sessionId: String?,
        endpoint: String,
        ubusObject: String,
        ubusMethod: String,
        params: Map<String, Any?>
    ): String {
        val id = idGenerator.next()
        val requestBody = buildRequestBody(id, sessionId, ubusObject, ubusMethod, params)
        DebugLogger.d("UbusJsonRpcClient", "Request: ${SensitiveLogSanitizer.sanitizeRequestBodyForLogging(requestBody)}")

        val url = buildUrl(securityContext, endpoint)
        val httpRequest = Request.Builder()
            .url(url)
            .post(requestBody.toRequestBody(jsonMediaType))
            .build()

        val httpResponse = clientFactory.createClient(securityContext).newCall(httpRequest).execute()
        val responseBody = httpResponse.body?.string()
            ?: throw IOException("Empty response body")

        DebugLogger.d("UbusJsonRpcClient", "Response: ${SensitiveLogSanitizer.redactSensitiveFields(responseBody)}")
        return responseBody
    }

    private fun parseUbusResult(responseBody: String): AppResult<Any?> {
        val ubusResponse = parseResponse(responseBody)

        if (ubusResponse.error != null)
            return AppResult.Failure(AppError.UbusError(ubusResponse.error.code, ubusResponse.error.message))

        val result = ubusResponse.result ?: return AppResult.Success(null)

        if (result.isNotEmpty() && result[0] is Int) {
            val statusCode = result[0] as Int
            if (UbusStatusCode.fromCode(statusCode) == UbusStatusCode.OK) {
                val data = if (result.size > 1) result[1] else null
                return AppResult.Success(data)
            }
            val errorMessage = if (result.size > 1) result[1]?.toString() else null
            return AppResult.Failure(AppError.UbusError(statusCode, errorMessage))
        }

        return AppResult.Success(result)
    }

    private fun buildRequestBody(
        id: Long,
        sessionId: String?,
        ubusObject: String,
        ubusMethod: String,
        params: Map<String, Any?>
    ): String {
        val paramsArray = JSONArray()

        if (sessionId != null) {
            paramsArray.put(sessionId)
            paramsArray.put(ubusObject)
            paramsArray.put(ubusMethod)
        } else {
            paramsArray.put(ubusMethod)
        }

        val paramObj = JSONObject()
        for ((key, value) in params) {
            when (value) {
                null -> paramObj.put(key, JSONObject.NULL)
                is String -> paramObj.put(key, value)
                is Number -> paramObj.put(key, value)
                is Boolean -> paramObj.put(key, value)
                is JSONObject -> paramObj.put(key, value)
                is JSONArray -> paramObj.put(key, value)
                else -> paramObj.put(key, value.toString())
            }
        }
        paramsArray.put(paramObj)

        val requestObj = JSONObject()
        requestObj.put("jsonrpc", "2.0")
        requestObj.put("id", id)

        if (sessionId != null) {
            requestObj.put("method", "session")
        } else {
            requestObj.put("method", ubusObject)
        }

        requestObj.put("params", paramsArray)

        return requestObj.toString()
    }

    private fun buildUrl(
        securityContext: RouterSecurityContext,
        endpoint: String
    ): String {
        val scheme = securityContext.scheme.name.lowercase()
        val path = if (endpoint.startsWith("/")) endpoint else "/$endpoint"
        return "${scheme}://${securityContext.host}:${securityContext.port}${path}"
    }

    private fun parseResponse(responseBody: String): UbusResponse {
        val json = JSONObject(responseBody)
        val id = json.optLong("id", -1)
        val jsonrpc = json.optString("jsonrpc", null as String?)
        val error = json.optJSONObject("error")?.let {
            UbusRpcError(it.optInt("code"), it.optString("message", null as String?))
        }
        val resultArray = json.optJSONArray("result")
        val result: List<Any?>? = when {
            resultArray == null -> null
            else -> {
                val list = mutableListOf<Any?>()
                for (i in 0 until resultArray.length()) {
                    list.add(convertJsonValue(resultArray.get(i)))
                }
                list
            }
        }
        return UbusResponse(id, jsonrpc, result, error)
    }

    private fun convertJsonValue(value: Any?): Any? {
        return when (value) {
            is org.json.JSONObject -> {
                val map = mutableMapOf<String, Any?>()
                for (key in value.keys()) {
                    map[key] = convertJsonValue(value.get(key))
                }
                map
            }
            is org.json.JSONArray -> {
                val list = mutableListOf<Any?>()
                for (i in 0 until value.length()) {
                    list.add(convertJsonValue(value.get(i)))
                }
                list
            }
            JSONObject.NULL -> null
            else -> value
        }
    }
}
