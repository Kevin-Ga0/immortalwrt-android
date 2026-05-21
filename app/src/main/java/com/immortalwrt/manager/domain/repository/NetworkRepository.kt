package com.immortalwrt.manager.domain.repository

import com.immortalwrt.manager.domain.model.AppResult
import com.immortalwrt.manager.domain.model.NetworkInterface
import com.immortalwrt.manager.domain.model.RouterSecurityContext

interface NetworkRepository {
    suspend fun getInterfaces(routerId: String): AppResult<List<NetworkInterface>>
    suspend fun getCachedInterfaces(routerId: String): List<NetworkInterface>
    suspend fun refreshInterfaces(
        routerId: String,
        securityContext: RouterSecurityContext,
        endpoint: String,
        sessionId: String
    ): AppResult<List<NetworkInterface>>
}
