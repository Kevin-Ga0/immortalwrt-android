package com.immortalwrt.manager.di;

import com.immortalwrt.manager.data.local.AppDatabase;
import com.immortalwrt.manager.data.local.dao.RouterCacheDao;
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
public final class DatabaseModule_ProvideRouterCacheDaoFactory implements Factory<RouterCacheDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideRouterCacheDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public RouterCacheDao get() {
    return provideRouterCacheDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideRouterCacheDaoFactory create(
      Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideRouterCacheDaoFactory(dbProvider);
  }

  public static RouterCacheDao provideRouterCacheDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideRouterCacheDao(db));
  }
}
