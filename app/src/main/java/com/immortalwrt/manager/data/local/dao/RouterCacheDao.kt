package com.immortalwrt.manager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.immortalwrt.manager.data.local.entity.DeviceCacheEntity
import com.immortalwrt.manager.data.local.entity.InterfaceCacheEntity
import com.immortalwrt.manager.data.local.entity.SystemSnapshotEntity

@Dao
interface RouterCacheDao {
    @Query("SELECT * FROM system_snapshots WHERE router_id = :router_id")
    suspend fun getSystemSnapshot(router_id: String): SystemSnapshotEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSystemSnapshot(snapshot: SystemSnapshotEntity)

    @Query("DELETE FROM system_snapshots WHERE router_id = :router_id")
    suspend fun deleteSystemSnapshot(router_id: String)

    @Query("SELECT * FROM interface_caches WHERE router_id = :router_id")
    suspend fun getInterfaceCaches(router_id: String): List<InterfaceCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertInterfaces(interfaces: List<InterfaceCacheEntity>)

    @Query("DELETE FROM interface_caches WHERE router_id = :router_id")
    suspend fun deleteInterfaceCaches(router_id: String)

    @Query("SELECT * FROM device_caches WHERE router_id = :router_id")
    suspend fun getDeviceCaches(router_id: String): List<DeviceCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDevices(devices: List<DeviceCacheEntity>)

    @Query("DELETE FROM device_caches WHERE router_id = :router_id")
    suspend fun deleteDeviceCaches(router_id: String)
}
