package com.immortalwrt.manager.ui.screens.dashboard;

import com.immortalwrt.manager.domain.repository.RouterRepository;
import com.immortalwrt.manager.domain.repository.SystemRepository;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<SystemRepository> systemRepositoryProvider;

  private final Provider<RouterRepository> routerRepositoryProvider;

  public DashboardViewModel_Factory(Provider<SystemRepository> systemRepositoryProvider,
      Provider<RouterRepository> routerRepositoryProvider) {
    this.systemRepositoryProvider = systemRepositoryProvider;
    this.routerRepositoryProvider = routerRepositoryProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(systemRepositoryProvider.get(), routerRepositoryProvider.get());
  }

  public static DashboardViewModel_Factory create(
      Provider<SystemRepository> systemRepositoryProvider,
      Provider<RouterRepository> routerRepositoryProvider) {
    return new DashboardViewModel_Factory(systemRepositoryProvider, routerRepositoryProvider);
  }

  public static DashboardViewModel newInstance(SystemRepository systemRepository,
      RouterRepository routerRepository) {
    return new DashboardViewModel(systemRepository, routerRepository);
  }
}
