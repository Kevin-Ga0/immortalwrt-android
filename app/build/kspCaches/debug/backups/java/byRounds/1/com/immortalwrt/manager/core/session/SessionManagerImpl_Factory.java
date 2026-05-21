package com.immortalwrt.manager.core.session;

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
public final class SessionManagerImpl_Factory implements Factory<SessionManagerImpl> {
  private final Provider<UbusJsonRpcClient> rpcClientProvider;

  public SessionManagerImpl_Factory(Provider<UbusJsonRpcClient> rpcClientProvider) {
    this.rpcClientProvider = rpcClientProvider;
  }

  @Override
  public SessionManagerImpl get() {
    return newInstance(rpcClientProvider.get());
  }

  public static SessionManagerImpl_Factory create(Provider<UbusJsonRpcClient> rpcClientProvider) {
    return new SessionManagerImpl_Factory(rpcClientProvider);
  }

  public static SessionManagerImpl newInstance(UbusJsonRpcClient rpcClient) {
    return new SessionManagerImpl(rpcClient);
  }
}
