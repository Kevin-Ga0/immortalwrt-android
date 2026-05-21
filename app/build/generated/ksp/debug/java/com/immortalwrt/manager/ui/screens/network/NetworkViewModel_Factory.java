package com.immortalwrt.manager.ui.screens.network;

import com.immortalwrt.manager.domain.repository.NetworkRepository;
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
public final class NetworkViewModel_Factory implements Factory<NetworkViewModel> {
  private final Provider<NetworkRepository> networkRepositoryProvider;

  public NetworkViewModel_Factory(Provider<NetworkRepository> networkRepositoryProvider) {
    this.networkRepositoryProvider = networkRepositoryProvider;
  }

  @Override
  public NetworkViewModel get() {
    return newInstance(networkRepositoryProvider.get());
  }

  public static NetworkViewModel_Factory create(
      Provider<NetworkRepository> networkRepositoryProvider) {
    return new NetworkViewModel_Factory(networkRepositoryProvider);
  }

  public static NetworkViewModel newInstance(NetworkRepository networkRepository) {
    return new NetworkViewModel(networkRepository);
  }
}
