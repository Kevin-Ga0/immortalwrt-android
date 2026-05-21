package com.immortalwrt.manager.domain.repository

import com.immortalwrt.manager.core.network.EndpointProbeResult
import com.immortalwrt.manager.domain.model.AppResult
import com.immortalwrt.manager.domain.model.Router
import kotlinx.coroutines.flow.Flow

interface RouterRepository {
    fun observeAll(): Flow<List<Router>>
    suspend fun getById(id: String): Router?
    suspend fun addRouter(router: Router, password: String): AppResult<Router>
    suspend fun updateRouter(router: Router, password: String? = null): AppResult<Router>
    suspend fun deleteRouter(id: String): AppResult<Unit>
    suspend fun testConnection(routerId: String): AppResult<EndpointProbeResult>
}
