package com.immortalwrt.manager.domain.repository

import com.immortalwrt.manager.domain.model.AppResult
import com.immortalwrt.manager.domain.model.ConnectedDevice
import com.immortalwrt.manager.domain.model.RouterSecurityContext

interface DeviceRepository {
    suspend fun getDevices(routerId: String): AppResult<List<ConnectedDevice>>
    suspend fun getCachedDevices(routerId: String): List<ConnectedDevice>
    suspend fun refreshDevices(
        routerId: String,
        securityContext: RouterSecurityContext,
        endpoint: String,
        sessionId: String
    ): AppResult<List<ConnectedDevice>>
}
