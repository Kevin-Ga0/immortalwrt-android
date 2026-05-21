package com.immortalwrt.manager.data.repository;

import com.immortalwrt.manager.core.network.UbusJsonRpcClient;
import com.immortalwrt.manager.data.local.dao.RouterCacheDao;
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
public final class DeviceRepositoryImpl_Factory implements Factory<DeviceRepositoryImpl> {
  private final Provider<UbusJsonRpcClient> rpcClientProvider;

  private final Provider<RouterCacheDao> routerCacheDaoProvider;

  public DeviceRepositoryImpl_Factory(Provider<UbusJsonRpcClient> rpcClientProvider,
      Provider<RouterCacheDao> routerCacheDaoProvider) {
    this.rpcClientProvider = rpcClientProvider;
    this.routerCacheDaoProvider = routerCacheDaoProvider;
  }

  @Override
  public DeviceRepositoryImpl get() {
    return newInstance(rpcClientProvider.get(), routerCacheDaoProvider.get());
  }

  public static DeviceRepositoryImpl_Factory create(Provider<UbusJsonRpcClient> rpcClientProvider,
      Provider<RouterCacheDao> routerCacheDaoProvider) {
    return new DeviceRepositoryImpl_Factory(rpcClientProvider, routerCacheDaoProvider);
  }

  public static DeviceRepositoryImpl newInstance(UbusJsonRpcClient rpcClient,
      RouterCacheDao routerCacheDao) {
    return new DeviceRepositoryImpl(rpcClient, routerCacheDao);
  }
}
