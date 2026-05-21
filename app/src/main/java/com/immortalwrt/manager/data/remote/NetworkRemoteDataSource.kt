package com.immortalwrt.manager.data.remote

import com.immortalwrt.manager.core.network.UbusJsonRpcClient
import com.immortalwrt.manager.domain.model.AppResult
import com.immortalwrt.manager.domain.model.RouterSecurityContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRemoteDataSource @Inject constructor(
    private val rpcClient: UbusJsonRpcClient
) {
    suspend fun fetchInterfaceDump(
        securityContext: RouterSecurityContext,
        endpoint: String,
        sessionId: String
    ): AppResult<Any?> {
        return rpcClient.call(
            securityContext, sessionId, endpoint, "network.interface", "dump", emptyMap()
        )
    }

    suspend fun fetchInterfaceStatus(
        securityContext: RouterSecurityContext,
        endpoint: String,
        sessionId: String,
        ifname: String
    ): AppResult<Any?> {
        return rpcClient.call(
            securityContext, sessionId, endpoint, "network.interface.$ifname", "status", emptyMap()
        )
    }
}
