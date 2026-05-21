package com.immortalwrt.manager.core.session

import com.immortalwrt.manager.domain.model.AppResult
import com.immortalwrt.manager.domain.model.RouterSecurityContext
import com.immortalwrt.manager.domain.model.RouterSession

interface SessionManager {
    suspend fun getSession(routerId: String): AppResult<RouterSession>

    suspend fun login(
        routerId: String,
        securityContext: RouterSecurityContext,
        endpoint: String,
        username: String,
        password: String
    ): AppResult<RouterSession>

    suspend fun invalidate(routerId: String)

    suspend fun destroy(routerId: String)
}
