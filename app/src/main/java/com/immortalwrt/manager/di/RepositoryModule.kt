package com.immortalwrt.manager.di

import com.immortalwrt.manager.core.security.AndroidKeystoreSecretStore
import com.immortalwrt.manager.core.security.SecretStore
import com.immortalwrt.manager.core.session.SessionManager
import com.immortalwrt.manager.core.session.SessionManagerImpl
import com.immortalwrt.manager.data.repository.*
import com.immortalwrt.manager.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSecretStore(impl: AndroidKeystoreSecretStore): SecretStore

    @Binds
    @Singleton
    abstract fun bindSessionManager(impl: SessionManagerImpl): SessionManager

    @Binds
    @Singleton
    abstract fun bindRouterRepository(impl: RouterRepositoryImpl): RouterRepository

    @Binds
    @Singleton
    abstract fun bindCapabilityRepository(impl: CapabilityRepositoryImpl): CapabilityRepository

    @Binds
    @Singleton
    abstract fun bindSystemRepository(impl: SystemRepositoryImpl): SystemRepository

    @Binds
    @Singleton
    abstract fun bindNetworkRepository(impl: NetworkRepositoryImpl): NetworkRepository

    @Binds
    @Singleton
    abstract fun bindDeviceRepository(impl: DeviceRepositoryImpl): DeviceRepository
}
