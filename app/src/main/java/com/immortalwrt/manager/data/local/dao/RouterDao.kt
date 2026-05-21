package com.immortalwrt.manager.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.immortalwrt.manager.data.local.entity.RouterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RouterDao {
    @Query("SELECT * FROM routers ORDER BY created_at DESC")
    fun observeAll(): Flow<List<RouterEntity>>

    @Query("SELECT * FROM routers WHERE id = :id")
    suspend fun getById(id: String): RouterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(router: RouterEntity)

    @Delete
    suspend fun delete(router: RouterEntity)

    @Query("DELETE FROM routers WHERE id = :id")
    suspend fun deleteById(id: String)
}
