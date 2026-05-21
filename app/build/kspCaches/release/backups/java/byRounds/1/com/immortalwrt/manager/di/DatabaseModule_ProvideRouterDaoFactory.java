package com.immortalwrt.manager.di;

import com.immortalwrt.manager.data.local.AppDatabase;
import com.immortalwrt.manager.data.local.dao.RouterDao;
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
public final class DatabaseModule_ProvideRouterDaoFactory implements Factory<RouterDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideRouterDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public RouterDao get() {
    return provideRouterDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideRouterDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideRouterDaoFactory(dbProvider);
  }

  public static RouterDao provideRouterDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideRouterDao(db));
  }
}
