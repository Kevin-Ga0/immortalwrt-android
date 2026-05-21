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
public final class SystemRepositoryImpl_Factory implements Factory<SystemRepositoryImpl> {
  private final Provider<UbusJsonRpcClient> rpcClientProvider;

  private final Provider<RouterCacheDao> cacheDaoProvider;

  public SystemRepositoryImpl_Factory(Provider<UbusJsonRpcClient> rpcClientProvider,
      Provider<RouterCacheDao> cacheDaoProvider) {
    this.rpcClientProvider = rpcClientProvider;
    this.cacheDaoProvider = cacheDaoProvider;
  }

  @Override
  public SystemRepositoryImpl get() {
    return newInstance(rpcClientProvider.get(), cacheDaoProvider.get());
  }

  public static SystemRepositoryImpl_Factory create(Provider<UbusJsonRpcClient> rpcClientProvider,
      Provider<RouterCacheDao> cacheDaoProvider) {
    return new SystemRepositoryImpl_Factory(rpcClientProvider, cacheDaoProvider);
  }

  public static SystemRepositoryImpl newInstance(UbusJsonRpcClient rpcClient,
      RouterCacheDao cacheDao) {
    return new SystemRepositoryImpl(rpcClient, cacheDao);
  }
}
