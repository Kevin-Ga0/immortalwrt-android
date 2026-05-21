package com.immortalwrt.manager.ui.screens.system;

import com.immortalwrt.manager.domain.repository.CapabilityRepository;
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
public final class SystemViewModel_Factory implements Factory<SystemViewModel> {
  private final Provider<SystemRepository> systemRepositoryProvider;

  private final Provider<CapabilityRepository> capabilityRepositoryProvider;

  public SystemViewModel_Factory(Provider<SystemRepository> systemRepositoryProvider,
      Provider<CapabilityRepository> capabilityRepositoryProvider) {
    this.systemRepositoryProvider = systemRepositoryProvider;
    this.capabilityRepositoryProvider = capabilityRepositoryProvider;
  }

  @Override
  public SystemViewModel get() {
    return newInstance(systemRepositoryProvider.get(), capabilityRepositoryProvider.get());
  }

  public static SystemViewModel_Factory create(Provider<SystemRepository> systemRepositoryProvider,
      Provider<CapabilityRepository> capabilityRepositoryProvider) {
    return new SystemViewModel_Factory(systemRepositoryProvider, capabilityRepositoryProvider);
  }

  public static SystemViewModel newInstance(SystemRepository systemRepository,
      CapabilityRepository capabilityRepository) {
    return new SystemViewModel(systemRepository, capabilityRepository);
  }
}
