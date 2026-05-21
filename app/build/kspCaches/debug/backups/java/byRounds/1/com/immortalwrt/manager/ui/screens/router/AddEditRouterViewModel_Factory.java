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
public final class AddEditRouterViewModel_Factory implements Factory<AddEditRouterViewModel> {
  private final Provider<RouterRepository> routerRepositoryProvider;

  public AddEditRouterViewModel_Factory(Provider<RouterRepository> routerRepositoryProvider) {
    this.routerRepositoryProvider = routerRepositoryProvider;
  }

  @Override
  public AddEditRouterViewModel get() {
    return newInstance(routerRepositoryProvider.get());
  }

  public static AddEditRouterViewModel_Factory create(
      Provider<RouterRepository> routerRepositoryProvider) {
    return new AddEditRouterViewModel_Factory(routerRepositoryProvider);
  }

  public static AddEditRouterViewModel newInstance(RouterRepository routerRepository) {
    return new AddEditRouterViewModel(routerRepository);
  }
}
