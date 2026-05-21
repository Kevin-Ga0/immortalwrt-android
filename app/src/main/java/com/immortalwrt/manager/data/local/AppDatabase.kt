package com.immortalwrt.manager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.immortalwrt.manager.data.local.dao.DiagnosticEventDao
import com.immortalwrt.manager.data.local.dao.RouterCacheDao
import com.immortalwrt.manager.data.local.dao.RouterCapabilityDao
import com.immortalwrt.manager.data.local.dao.RouterDao
import com.immortalwrt.manager.data.local.dao.RouterEnvironmentDao
import com.immortalwrt.manager.data.local.entity.DeviceCacheEntity
import com.immortalwrt.manager.data.local.entity.DiagnosticEventEntity
import com.immortalwrt.manager.data.local.entity.InterfaceCacheEntity
import com.immortalwrt.manager.data.local.entity.RouterCapabilityEntity
import com.immortalwrt.manager.data.local.entity.RouterEntity
import com.immortalwrt.manager.data.local.entity.RouterEnvironmentEntity
import com.immortalwrt.manager.data.local.entity.SystemSnapshotEntity

@Database(
    entities = [
        RouterEntity::class,
        RouterEnvironmentEntity::class,
        RouterCapabilityEntity::class,
        SystemSnapshotEntity::class,
        InterfaceCacheEntity::class,
        DeviceCacheEntity::class,
        DiagnosticEventEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routerDao(): RouterDao
    abstract fun routerEnvironmentDao(): RouterEnvironmentDao
    abstract fun routerCapabilityDao(): RouterCapabilityDao
    abstract fun routerCacheDao(): RouterCacheDao
    abstract fun diagnosticEventDao(): DiagnosticEventDao
}
