package com.immortalwrt.manager.domain.repository

import com.immortalwrt.manager.domain.model.AppResult
import com.immortalwrt.manager.domain.model.RouterCapability
import com.immortalwrt.manager.domain.model.RouterEnvironment
import com.immortalwrt.manager.domain.model.RouterSecurityContext

interface CapabilityRepository {
    suspend fun getCapability(routerId: String): RouterCapability?
    suspend fun probeCapability(
        routerId: String,
        securityContext: RouterSecurityContext,
        endpoint: String,
        sessionId: String
    ): AppResult<RouterCapability>
    suspend fun getEnvironment(routerId: String): RouterEnvironment?
    suspend fun saveEnvironment(routerId: String, environment: RouterEnvironment)
}
