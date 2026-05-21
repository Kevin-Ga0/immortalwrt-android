package com.immortalwrt.manager.di

import android.content.Context
import androidx.room.Room
import com.immortalwrt.manager.data.local.AppDatabase
import com.immortalwrt.manager.data.local.dao.DiagnosticEventDao
import com.immortalwrt.manager.data.local.dao.RouterCacheDao
import com.immortalwrt.manager.data.local.dao.RouterCapabilityDao
import com.immortalwrt.manager.data.local.dao.RouterDao
import com.immortalwrt.manager.data.local.dao.RouterEnvironmentDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "immortalwrt_manager.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideRouterDao(db: AppDatabase): RouterDao = db.routerDao()
    @Provides fun provideRouterEnvironmentDao(db: AppDatabase): RouterEnvironmentDao = db.routerEnvironmentDao()
    @Provides fun provideRouterCapabilityDao(db: AppDatabase): RouterCapabilityDao = db.routerCapabilityDao()
    @Provides fun provideRouterCacheDao(db: AppDatabase): RouterCacheDao = db.routerCacheDao()
    @Provides fun provideDiagnosticEventDao(db: AppDatabase): DiagnosticEventDao = db.diagnosticEventDao()
}
