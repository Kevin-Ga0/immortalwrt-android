package com.immortalwrt.manager.data.remote

import com.immortalwrt.manager.core.network.UbusJsonRpcClient
import com.immortalwrt.manager.domain.model.AppResult
import com.immortalwrt.manager.domain.model.RouterSecurityContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRemoteDataSource @Inject constructor(
    private val rpcClient: UbusJsonRpcClient
) {
    suspend fun fetchDhcpLeases(
        securityContext: RouterSecurityContext,
        endpoint: String,
        sessionId: String
    ): AppResult<Any?> {
        return rpcClient.call(
            securityContext, sessionId, endpoint,
            "luci-rpc", "getDHCPLeases", mapOf("family" to 4)
        )
    }

    suspend fun fetchHostHints(
        securityContext: RouterSecurityContext,
        endpoint: String,
        sessionId: String
    ): AppResult<Any?> {
        return rpcClient.call(
            securityContext, sessionId, endpoint,
            "luci-rpc", "getHostHints", emptyMap()
        )
    }
}
