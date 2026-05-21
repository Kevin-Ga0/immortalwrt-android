package com.immortalwrt.manager.di;

import com.immortalwrt.manager.data.local.AppDatabase;
import com.immortalwrt.manager.data.local.dao.RouterEnvironmentDao;
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
public final class DatabaseModule_ProvideRouterEnvironmentDaoFactory implements Factory<RouterEnvironmentDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideRouterEnvironmentDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public RouterEnvironmentDao get() {
    return provideRouterEnvironmentDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideRouterEnvironmentDaoFactory create(
      Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideRouterEnvironmentDaoFactory(dbProvider);
  }

  public static RouterEnvironmentDao provideRouterEnvironmentDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideRouterEnvironmentDao(db));
  }
}
