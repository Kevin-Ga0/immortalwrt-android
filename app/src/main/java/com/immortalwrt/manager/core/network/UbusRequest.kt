package com.immortalwrt.manager.core.network

data class UbusRequest(
    val id: Long,
    val jsonrpc: String = "2.0",
    val method: String,
    val params: List<Any?>
)
