package com.immortalwrt.manager.di;

import com.immortalwrt.manager.data.local.AppDatabase;
import com.immortalwrt.manager.data.local.dao.RouterCapabilityDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideRouterCapabilityDaoFactory implements Factory<RouterCapabilityDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideRouterCapabilityDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public RouterCapabilityDao get() {
    return provideRouterCapabilityDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideRouterCapabilityDaoFactory create(
      Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideRouterCapabilityDaoFactory(dbProvider);
  }

  public static RouterCapabilityDao provideRouterCapabilityDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideRouterCapabilityDao(db));
  }
}
