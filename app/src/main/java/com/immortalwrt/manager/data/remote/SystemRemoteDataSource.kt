package com.immortalwrt.manager.data.remote

import com.immortalwrt.manager.core.network.UbusJsonRpcClient
import com.immortalwrt.manager.domain.model.AppResult
import com.immortalwrt.manager.domain.model.RouterSecurityContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SystemRemoteDataSource @Inject constructor(
    private val rpcClient: UbusJsonRpcClient
) {
    suspend fun fetchBoard(
        securityContext: RouterSecurityContext,
        endpoint: String,
        sessionId: String
    ): AppResult<Any?> = rpcClient.call(
        securityContext, sessionId, endpoint, "system", "board", emptyMap()
    )

    suspend fun fetchInfo(
        securityContext: RouterSecurityContext,
        endpoint: String,
        sessionId: String
    ): AppResult<Any?> = rpcClient.call(
        securityContext, sessionId, endpoint, "system", "info", emptyMap()
    )

    suspend fun fetchTempInfo(
        securityContext: RouterSecurityContext,
        endpoint: String,
        sessionId: String
    ): AppResult<Any?> = rpcClient.call(
        securityContext, sessionId, endpoint, "luci", "getTempInfo", emptyMap()
    )

    suspend fun fetchCpuUsage(
        securityContext: RouterSecurityContext,
        endpoint: String,
        sessionId: String
    ): AppResult<Any?> = rpcClient.call(
        securityContext, sessionId, endpoint, "luci", "getCPUUsage", emptyMap()
    )
}
