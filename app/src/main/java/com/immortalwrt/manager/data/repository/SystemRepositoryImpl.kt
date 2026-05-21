package com.immortalwrt.manager.data.repository

import com.immortalwrt.manager.core.network.UbusJsonRpcClient
import com.immortalwrt.manager.data.local.dao.RouterCacheDao
import com.immortalwrt.manager.data.local.entity.SystemSnapshotEntity
import com.immortalwrt.manager.domain.model.AppError
import com.immortalwrt.manager.domain.model.AppResult
import com.immortalwrt.manager.domain.model.RouterSecurityContext
import com.immortalwrt.manager.domain.model.SystemInfo
import com.immortalwrt.manager.domain.repository.SystemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SystemRepositoryImpl @Inject constructor(
    private val rpcClient: UbusJsonRpcClient,
    private val cacheDao: RouterCacheDao
) : SystemRepository {

    override suspend fun getSystemInfo(routerId: String): AppResult<SystemInfo> {
        return withContext(Dispatchers.IO) {
            val cached = cacheDao.getSystemSnapshot(routerId)
            if (cached != null) {
                AppResult.Success(cached.toDomain())
            } else {
                AppResult.Failure(AppError.Unknown(null))
            }
        }
    }

    override suspend fun getCachedSystemInfo(routerId: String): SystemInfo? {
        return withContext(Dispatchers.IO) {
            cacheDao.getSystemSnapshot(routerId)?.toDomain()
        }
    }

    override suspend fun refreshSystemInfo(
        routerId: String,
        securityContext: RouterSecurityContext,
        endpoint: String,
        sessionId: String
    ): AppResult<SystemInfo> = withContext(Dispatchers.IO) {
        val boardResult = rpcClient.call(
            securityContext, sessionId, endpoint, "system", "board", emptyMap()
        )
        if (boardResult is AppResult.Failure) {
            return@withContext boardResult
        }
        val boardData = (boardResult as AppResult.Success).data as? Map<*, *>
        if (boardData == null) {
            return@withContext AppResult.Failure(AppError.JsonParseError(null))
        }

        val infoResult = rpcClient.call(
            securityContext, sessionId, endpoint, "system", "info", emptyMap()
        )
        if (infoResult is AppResult.Failure) {
            return@withContext infoResult
        }
        val infoData = (infoResult as AppResult.Success).data as? Map<*, *>
        if (infoData == null) {
            return@withContext AppResult.Failure(AppError.JsonParseError(null))
        }

        val hostname = boardData["hostname"] as? String
        val model = boardData["model"] as? String
        val architecture = boardData["system"] as? String
        val kernel = boardData["kernel"] as? String
        val release = boardData["release"] as? Map<*, *>
        val targetPlatform = release?.get("target") as? String
        val firmwareVersion = release?.get("description") as? String

        val uptime = (infoData["uptime"] as? Number)?.toLong()
        val localTime = (infoData["localtime"] as? Number)?.toLong()
        val loadArray = infoData["load"] as? List<*>
        val loadAverage = loadArray?.mapNotNull { value ->
            (value as? Number)?.toFloat()?.div(65535.0f)
        } ?: emptyList()

        val memory = infoData["memory"] as? Map<*, *>
        val memoryTotal = (memory?.get("total") as? Number)?.toLong() ?: 0L
        val memoryFree = (memory?.get("free") as? Number)?.toLong() ?: 0L
        val memoryBuffered = (memory?.get("buffered") as? Number)?.toLong() ?: 0L

        val swap = infoData["swap"] as? Map<*, *>
        val swapTotal = (swap?.get("total") as? Number)?.toLong() ?: 0L
        val swapFree = (swap?.get("free") as? Number)?.toLong() ?: 0L

        val temperature = fetchOptionalTempInfo(securityContext, endpoint, sessionId)
        val cpuUsage = fetchOptionalCpuUsage(securityContext, endpoint, sessionId)

        val systemInfo = SystemInfo(
            hostname = hostname,
            model = model,
            architecture = architecture,
            targetPlatform = targetPlatform,
            firmwareVersion = firmwareVersion,
            kernelVersion = kernel,
            localTime = localTime,
            uptime = uptime,
            loadAverage = loadAverage,
            memoryTotal = memoryTotal,
            memoryFree = memoryFree,
            memoryBuffered = memoryBuffered,
            swapTotal = swapTotal,
            swapFree = swapFree,
            temperature = temperature,
            cpuUsage = cpuUsage
        )

        val snapshot = systemInfo.toEntity(routerId)
        cacheDao.upsertSystemSnapshot(snapshot)

        AppResult.Success(systemInfo)
    }

    private suspend fun fetchOptionalTempInfo(
        securityContext: RouterSecurityContext,
        endpoint: String,
        sessionId: String
    ): String? {
        return try {
            val result = rpcClient.call(
                securityContext, sessionId, endpoint, "luci", "getTempInfo", emptyMap()
            )
            if (result is AppResult.Success) {
                val data = result.data as? Map<*, *>
                data?.values?.firstOrNull()?.toString()
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    private suspend fun fetchOptionalCpuUsage(
        securityContext: RouterSecurityContext,
        endpoint: String,
        sessionId: String
    ): String? {
        return try {
            val result = rpcClient.call(
                securityContext, sessionId, endpoint, "luci", "getCPUUsage", emptyMap()
            )
            if (result is AppResult.Success) {
                val data = result.data as? Map<*, *>
                data?.values?.firstOrNull()?.toString()
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }
}

private fun SystemSnapshotEntity.toDomain(): SystemInfo = SystemInfo(
    hostname = hostname,
    model = model,
    architecture = architecture,
    targetPlatform = targetPlatform,
    firmwareVersion = firmwareVersion,
    kernelVersion = kernelVersion,
    localTime = localTime,
    uptime = uptime,
    loadAverage = loadAverage,
    memoryTotal = memoryTotal,
    memoryFree = memoryFree,
    memoryBuffered = memoryBuffered,
    swapTotal = swapTotal,
    swapFree = swapFree,
    temperature = temperature,
    cpuUsage = cpuUsage
)

private fun SystemInfo.toEntity(routerId: String): SystemSnapshotEntity = SystemSnapshotEntity(
    routerId = routerId,
    hostname = hostname,
    model = model,
    architecture = architecture,
    targetPlatform = targetPlatform,
    firmwareVersion = firmwareVersion,
    kernelVersion = kernelVersion,
    localTime = localTime,
    uptime = uptime,
    loadAverage = loadAverage,
    memoryTotal = memoryTotal,
    memoryFree = memoryFree,
    memoryBuffered = memoryBuffered,
    swapTotal = swapTotal,
    swapFree = swapFree,
    temperature = temperature,
    cpuUsage = cpuUsage,
    updatedAt = System.currentTimeMillis()
)
