package com.immortalwrt.manager.core.network;

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
public final class UbusJsonRpcClient_Factory implements Factory<UbusJsonRpcClient> {
  private final Provider<RouterOkHttpClientFactory> clientFactoryProvider;

  private final Provider<UbusCallIdGenerator> idGeneratorProvider;

  public UbusJsonRpcClient_Factory(Provider<RouterOkHttpClientFactory> clientFactoryProvider,
      Provider<UbusCallIdGenerator> idGeneratorProvider) {
    this.clientFactoryProvider = clientFactoryProvider;
    this.idGeneratorProvider = idGeneratorProvider;
  }

  @Override
  public UbusJsonRpcClient get() {
    return newInstance(clientFactoryProvider.get(), idGeneratorProvider.get());
  }

  public static UbusJsonRpcClient_Factory create(
      Provider<RouterOkHttpClientFactory> clientFactoryProvider,
      Provider<UbusCallIdGenerator> idGeneratorProvider) {
    return new UbusJsonRpcClient_Factory(clientFactoryProvider, idGeneratorProvider);
  }

  public static UbusJsonRpcClient newInstance(RouterOkHttpClientFactory clientFactory,
      UbusCallIdGenerator idGenerator) {
    return new UbusJsonRpcClient(clientFactory, idGenerator);
  }
}
