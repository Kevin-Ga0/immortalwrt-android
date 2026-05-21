package com.immortalwrt.manager.ui.screens.reboot;

import com.immortalwrt.manager.core.network.UbusJsonRpcClient;
import com.immortalwrt.manager.core.security.SecretStore;
import com.immortalwrt.manager.core.session.SessionManager;
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
public final class RebootViewModel_Factory implements Factory<RebootViewModel> {
  private final Provider<UbusJsonRpcClient> rpcClientProvider;

  private final Provider<SessionManager> sessionManagerProvider;

  private final Provider<SecretStore> secretStoreProvider;

  private final Provider<RouterRepository> routerRepositoryProvider;

  public RebootViewModel_Factory(Provider<UbusJsonRpcClient> rpcClientProvider,
      Provider<SessionManager> sessionManagerProvider, Provider<SecretStore> secretStoreProvider,
      Provider<RouterRepository> routerRepositoryProvider) {
    this.rpcClientProvider = rpcClientProvider;
    this.sessionManagerProvider = sessionManagerProvider;
    this.secretStoreProvider = secretStoreProvider;
    this.routerRepositoryProvider = routerRepositoryProvider;
  }

  @Override
  public RebootViewModel get() {
    return newInstance(rpcClientProvider.get(), sessionManagerProvider.get(), secretStoreProvider.get(), routerRepositoryProvider.get());
  }

  public static RebootViewModel_Factory create(Provider<UbusJsonRpcClient> rpcClientProvider,
      Provider<SessionManager> sessionManagerProvider, Provider<SecretStore> secretStoreProvider,
      Provider<RouterRepository> routerRepositoryProvider) {
    return new RebootViewModel_Factory(rpcClientProvider, sessionManagerProvider, secretStoreProvider, routerRepositoryProvider);
  }

  public static RebootViewModel newInstance(UbusJsonRpcClient rpcClient,
      SessionManager sessionManager, SecretStore secretStore, RouterRepository routerRepository) {
    return new RebootViewModel(rpcClient, sessionManager, secretStore, routerRepository);
  }
}
