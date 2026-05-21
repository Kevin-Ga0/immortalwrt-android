package com.immortalwrt.manager.domain.model

data class SystemInfo(
    val hostname: String? = null,
    val model: String? = null,
    val architecture: String? = null,
    val targetPlatform: String? = null,
    val firmwareVersion: String? = null,
    val kernelVersion: String? = null,
    val localTime: Long? = null,
    val uptime: Long? = null,
    val loadAverage: List<Float> = emptyList(),
    val memoryTotal: Long = 0,
    val memoryFree: Long = 0,
    val memoryBuffered: Long = 0,
    val swapTotal: Long = 0,
    val swapFree: Long = 0,
    val temperature: String? = null,
    val cpuUsage: String? = null
)
