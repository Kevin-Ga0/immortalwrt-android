package com.immortalwrt.manager.ui.screens.traffic;

import com.immortalwrt.manager.core.network.UbusJsonRpcClient;
import com.immortalwrt.manager.core.session.SessionManager;
import com.immortalwrt.manager.domain.repository.NetworkRepository;
import com.immortalwrt.manager.domain.repository.RouterRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class TrafficViewModel_Factory implements Factory<TrafficViewModel> {
  private final Provider<UbusJsonRpcClient> rpcClientProvider;

  private final Provider<SessionManager> sessionManagerProvider;

  private final Provider<RouterRepository> routerRepositoryProvider;

  private final Provider<NetworkRepository> networkRepositoryProvider;

  public TrafficViewModel_Factory(Provider<UbusJsonRpcClient> rpcClientProvider,
      Provider<SessionManager> sessionManagerProvider,
      Provider<RouterRepository> routerRepositoryProvider,
      Provider<NetworkRepository> networkRepositoryProvider) {
    this.rpcClientProvider = rpcClientProvider;
    this.sessionManagerProvider = sessionManagerProvider;
    this.routerRepositoryProvider = routerRepositoryProvider;
    this.networkRepositoryProvider = networkRepositoryProvider;
  }

  @Override
  public TrafficViewModel get() {
    return newInstance(rpcClientProvider.get(), sessionManagerProvider.get(), routerRepositoryProvider.get(), networkRepositoryProvider.get());
  }

  public static TrafficViewModel_Factory create(Provider<UbusJsonRpcClient> rpcClientProvider,
      Provider<SessionManager> sessionManagerProvider,
      Provider<RouterRepository> routerRepositoryProvider,
      Provider<NetworkRepository> networkRepositoryProvider) {
    return new TrafficViewModel_Factory(rpcClientProvider, sessionManagerProvider, routerRepositoryProvider, networkRepositoryProvider);
  }

  public static TrafficViewModel newInstance(UbusJsonRpcClient rpcClient,
      SessionManager sessionManager, RouterRepository routerRepository,
      NetworkRepository networkRepository) {
    return new TrafficViewModel(rpcClient, sessionManager, routerRepository, networkRepository);
  }
}
