package com.immortalwrt.manager.domain.repository

import com.immortalwrt.manager.domain.model.AppResult
import com.immortalwrt.manager.domain.model.RouterSecurityContext
import com.immortalwrt.manager.domain.model.SystemInfo

interface SystemRepository {
    suspend fun getSystemInfo(routerId: String): AppResult<SystemInfo>
    suspend fun getCachedSystemInfo(routerId: String): SystemInfo?
    suspend fun refreshSystemInfo(
        routerId: String,
        securityContext: RouterSecurityContext,
        endpoint: String,
        sessionId: String
    ): AppResult<SystemInfo>
}
