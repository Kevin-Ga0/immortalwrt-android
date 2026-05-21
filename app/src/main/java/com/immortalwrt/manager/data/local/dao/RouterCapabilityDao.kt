package com.immortalwrt.manager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.immortalwrt.manager.data.local.entity.RouterCapabilityEntity

@Dao
interface RouterCapabilityDao {
    @Query("SELECT * FROM router_capabilities WHERE router_id = :routerId")
    suspend fun getById(routerId: String): RouterCapabilityEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(cap: RouterCapabilityEntity)

    @Query("DELETE FROM router_capabilities WHERE router_id = :routerId")
    suspend fun deleteById(routerId: String)
}
