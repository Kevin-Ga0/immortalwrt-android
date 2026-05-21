package com.immortalwrt.manager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.immortalwrt.manager.data.local.entity.RouterEnvironmentEntity

@Dao
interface RouterEnvironmentDao {
    @Query("SELECT * FROM router_environments WHERE router_id = :routerId")
    suspend fun getById(routerId: String): RouterEnvironmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(env: RouterEnvironmentEntity)

    @Query("DELETE FROM router_environments WHERE router_id = :routerId")
    suspend fun deleteById(routerId: String)
}
