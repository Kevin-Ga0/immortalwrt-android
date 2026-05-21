package com.immortalwrt.manager.core.session

import com.immortalwrt.manager.core.network.UbusJsonRpcClient
import com.immortalwrt.manager.core.security.SensitiveLogSanitizer
import com.immortalwrt.manager.core.util.DebugLogger
import com.immortalwrt.manager.domain.model.AppError
import com.immortalwrt.manager.domain.model.AppResult
import com.immortalwrt.manager.domain.model.RouterSecurityContext
import com.immortalwrt.manager.domain.model.RouterSession
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Singleton
class SessionManagerImpl @Inject constructor(
    private val rpcClient: UbusJsonRpcClient
) : SessionManager {

    private val sessions = ConcurrentHashMap<String, RouterSession>()
    private val loginMutexes = ConcurrentHashMap<String, Mutex>()

    override suspend fun getSession(routerId: String): AppResult<RouterSession> {
        val session = sessions[routerId] ?: return AppResult.Failure(AppError.AuthFailed)

        val now = System.currentTimeMillis()
        val expiresAt = session.expiresAtEpochMillis

        if (expiresAt == null) {
            return AppResult.Success(session)
        }

        if (now >= expiresAt) {
            sessions.remove(routerId)
            return AppResult.Failure(AppError.SessionExpired)
        }

        // expiring_soon state — caller may choose to refresh
        return AppResult.Success(session)
    }

    override suspend fun login(
        routerId: String,
        securityContext: RouterSecurityContext,
        endpoint: String,
        username: String,
        password: String
    ): AppResult<RouterSession> {
        val mutex = loginMutexes.getOrPut(routerId) { Mutex() }

        return mutex.withLock {
            val existing = sessions[routerId]
            if (existing != null) {
                val now = System.currentTimeMillis()
                val expiresAt = existing.expiresAtEpochMillis
                if (expiresAt != null && now < expiresAt && (expiresAt - now) > 30_000) {
                    return@withLock AppResult.Success(existing)
                }
            }

            val params = mapOf<String, Any>(
                "username" to username,
                "password" to password,
                "timeout" to 300
            )

            val result = rpcClient.call(
                securityContext = securityContext,
                sessionId = null,
                endpoint = endpoint,
                ubusObject = "session",
                ubusMethod = "login",
                params = params
            )

            return@withLock when (result) {
                is AppResult.Failure -> {
                    val error = result.error
                    if (error is AppError.UbusError && error.code == 6) {
                        AppResult.Failure(AppError.PermissionDenied)
                    } else {
                        result
                    }
                }

                is AppResult.Success -> {
                    val data = result.data
                    @Suppress("UNCHECKED_CAST")
                    val responseData = data as? Map<*, *>
                        ?: return@withLock AppResult.Failure(
                            AppError.JsonParseError(data?.toString())
                        )

                    val sessionId = responseData["ubus_rpc_session"] as? String
                        ?: return@withLock AppResult.Failure(
                            AppError.JsonParseError(data?.toString())
                        )

                    val timeout = (responseData["timeout"] as? Number)?.toInt() ?: 300
                    val now = System.currentTimeMillis()

                    val routerSession = RouterSession(
                        routerId = routerId,
                        sessionId = sessionId,
                        timeoutSeconds = timeout,
                        expiresAtEpochMillis = now + (timeout * 1000L),
                        createdAtEpochMillis = now
                    )

                    sessions[routerId] = routerSession

                    DebugLogger.d(
                        "SessionManager",
                        "Login success for router $routerId, session=${SensitiveLogSanitizer.sanitizeSessionId(sessionId)}"
                    )

                    AppResult.Success(routerSession)
                }
            }
        }
    }

    override suspend fun invalidate(routerId: String) {
        val session = sessions.remove(routerId) ?: return
        loginMutexes.remove(routerId)
        DebugLogger.d(
            "SessionManager",
            "Invalidated session for router $routerId, session=${SensitiveLogSanitizer.sanitizeSessionId(session.sessionId)}"
        )
    }

    override suspend fun destroy(routerId: String) {
        val session = sessions.remove(routerId) ?: return
        loginMutexes.remove(routerId)
        DebugLogger.d(
            "SessionManager",
            "Destroyed session for router $routerId, session=${SensitiveLogSanitizer.sanitizeSessionId(session.sessionId)}"
        )
    }
}
