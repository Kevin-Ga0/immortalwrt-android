package com.immortalwrt.manager.data.repository;

import com.immortalwrt.manager.data.local.dao.RouterCacheDao;
import com.immortalwrt.manager.data.remote.NetworkRemoteDataSource;
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
public final class NetworkRepositoryImpl_Factory implements Factory<NetworkRepositoryImpl> {
  private final Provider<NetworkRemoteDataSource> remoteDataSourceProvider;

  private final Provider<RouterCacheDao> daoProvider;

  public NetworkRepositoryImpl_Factory(Provider<NetworkRemoteDataSource> remoteDataSourceProvider,
      Provider<RouterCacheDao> daoProvider) {
    this.remoteDataSourceProvider = remoteDataSourceProvider;
    this.daoProvider = daoProvider;
  }

  @Override
  public NetworkRepositoryImpl get() {
    return newInstance(remoteDataSourceProvider.get(), daoProvider.get());
  }

  public static NetworkRepositoryImpl_Factory create(
      Provider<NetworkRemoteDataSource> remoteDataSourceProvider,
      Provider<RouterCacheDao> daoProvider) {
    return new NetworkRepositoryImpl_Factory(remoteDataSourceProvider, daoProvider);
  }

  public static NetworkRepositoryImpl newInstance(NetworkRemoteDataSource remoteDataSource,
      RouterCacheDao dao) {
    return new NetworkRepositoryImpl(remoteDataSource, dao);
  }
}
