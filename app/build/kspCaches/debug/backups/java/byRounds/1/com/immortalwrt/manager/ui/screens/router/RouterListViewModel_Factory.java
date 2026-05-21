package com.immortalwrt.manager.ui.screens.router;

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
public final class RouterListViewModel_Factory implements Factory<RouterListViewModel> {
  private final Provider<RouterRepository> routerRepositoryProvider;

  public RouterListViewModel_Factory(Provider<RouterRepository> routerRepositoryProvider) {
    this.routerRepositoryProvider = routerRepositoryProvider;
  }

  @Override
  public RouterListViewModel get() {
    return newInstance(routerRepositoryProvider.get());
  }

  public static RouterListViewModel_Factory create(
      Provider<RouterRepository> routerRepositoryProvider) {
    return new RouterListViewModel_Factory(routerRepositoryProvider);
  }

  public static RouterListViewModel newInstance(RouterRepository routerRepository) {
    return new RouterListViewModel(routerRepository);
  }
}
