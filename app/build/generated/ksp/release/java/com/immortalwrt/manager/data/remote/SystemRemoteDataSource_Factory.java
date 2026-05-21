package com.immortalwrt.manager.data.remote;

import com.immortalwrt.manager.core.network.UbusJsonRpcClient;
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
public final class SystemRemoteDataSource_Factory implements Factory<SystemRemoteDataSource> {
  private final Provider<UbusJsonRpcClient> rpcClientProvider;

  public SystemRemoteDataSource_Factory(Provider<UbusJsonRpcClient> rpcClientProvider) {
    this.rpcClientProvider = rpcClientProvider;
  }

  @Override
  public SystemRemoteDataSource get() {
    return newInstance(rpcClientProvider.get());
  }

  public static SystemRemoteDataSource_Factory create(
      Provider<UbusJsonRpcClient> rpcClientProvider) {
    return new SystemRemoteDataSource_Factory(rpcClientProvider);
  }

  public static SystemRemoteDataSource newInstance(UbusJsonRpcClient rpcClient) {
    return new SystemRemoteDataSource(rpcClient);
  }
}
