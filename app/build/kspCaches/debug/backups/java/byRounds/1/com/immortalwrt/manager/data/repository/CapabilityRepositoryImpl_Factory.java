package com.immortalwrt.manager.data.repository;

import com.immortalwrt.manager.core.network.UbusJsonRpcClient;
import com.immortalwrt.manager.data.local.dao.RouterCapabilityDao;
import com.immortalwrt.manager.data.local.dao.RouterEnvironmentDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class CapabilityRepositoryImpl_Factory implements Factory<CapabilityRepositoryImpl> {
  private final Provider<RouterCapabilityDao> capabilityDaoProvider;

  private final Provider<RouterEnvironmentDao> environmentDaoProvider;

  private final Provider<UbusJsonRpcClient> rpcClientProvider;

  public CapabilityRepositoryImpl_Factory(Provider<RouterCapabilityDao> capabilityDaoProvider,
      Provider<RouterEnvironmentDao> environmentDaoProvider,
      Provider<UbusJsonRpcClient> rpcClientProvider) {
    this.capabilityDaoProvider = capabilityDaoProvider;
    this.environmentDaoProvider = environmentDaoProvider;
    this.rpcClientProvider = rpcClientProvider;
  }

  @Override
  public CapabilityRepositoryImpl get() {
    return newInstance(capabilityDaoProvider.get(), environmentDaoProvider.get(), rpcClientProvider.get());
  }

  public static CapabilityRepositoryImpl_Factory create(
      Provider<RouterCapabilityDao> capabilityDaoProvider,
      Provider<RouterEnvironmentDao> environmentDaoProvider,
      Provider<UbusJsonRpcClient> rpcClientProvider) {
    return new CapabilityRepositoryImpl_Factory(capabilityDaoProvider, environmentDaoProvider, rpcClientProvider);
  }

  public static CapabilityRepositoryImpl newInstance(RouterCapabilityDao capabilityDao,
      RouterEnvironmentDao environmentDao, UbusJsonRpcClient rpcClient) {
    return new CapabilityRepositoryImpl(capabilityDao, environmentDao, rpcClient);
  }
}
