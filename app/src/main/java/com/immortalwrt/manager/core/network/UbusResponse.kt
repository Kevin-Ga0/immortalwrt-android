package com.immortalwrt.manager.core.network

data class UbusResponse(
    val id: Long,
    val jsonrpc: String?,
    val result: List<Any?>?,
    val error: UbusRpcError?
)

data class UbusRpcError(
    val code: Int,
    val message: String?
)
