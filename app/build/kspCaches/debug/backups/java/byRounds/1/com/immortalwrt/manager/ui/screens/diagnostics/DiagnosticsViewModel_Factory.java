package com.immortalwrt.manager.ui.screens.diagnostics;

import com.immortalwrt.manager.domain.repository.CapabilityRepository;
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
public final class DiagnosticsViewModel_Factory implements Factory<DiagnosticsViewModel> {
  private final Provider<RouterRepository> routerRepositoryProvider;

  private final Provider<CapabilityRepository> capabilityRepositoryProvider;

  public DiagnosticsViewModel_Factory(Provider<RouterRepository> routerRepositoryProvider,
      Provider<CapabilityRepository> capabilityRepositoryProvider) {
    this.routerRepositoryProvider = routerRepositoryProvider;
    this.capabilityRepositoryProvider = capabilityRepositoryProvider;
  }

  @Override
  public DiagnosticsViewModel get() {
    return newInstance(routerRepositoryProvider.get(), capabilityRepositoryProvider.get());
  }

  public static DiagnosticsViewModel_Factory create(
      Provider<RouterRepository> routerRepositoryProvider,
      Provider<CapabilityRepository> capabilityRepositoryProvider) {
    return new DiagnosticsViewModel_Factory(routerRepositoryProvider, capabilityRepositoryProvider);
  }

  public static DiagnosticsViewModel newInstance(RouterRepository routerRepository,
      CapabilityRepository capabilityRepository) {
    return new DiagnosticsViewModel(routerRepository, capabilityRepository);
  }
}
