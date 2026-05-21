package com.immortalwrt.manager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.immortalwrt.manager.data.local.entity.DiagnosticEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiagnosticEventDao {
    @Query("SELECT * FROM diagnostic_events WHERE router_id = :router_id ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecent(router_id: String, limit: Int = 100): List<DiagnosticEventEntity>

    @Query("SELECT * FROM diagnostic_events WHERE router_id = :router_id ORDER BY timestamp DESC LIMIT :limit")
    fun observeRecent(router_id: String, limit: Int = 100): Flow<List<DiagnosticEventEntity>>

    @Insert
    suspend fun insert(event: DiagnosticEventEntity)

    @Query("DELETE FROM diagnostic_events WHERE router_id = :router_id")
    suspend fun deleteByRouter(router_id: String)

    @Query("DELETE FROM diagnostic_events")
    suspend fun deleteAll()
}
