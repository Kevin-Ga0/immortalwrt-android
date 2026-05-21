package com.immortalwrt.manager.di;

import com.immortalwrt.manager.data.local.AppDatabase;
import com.immortalwrt.manager.data.local.dao.DiagnosticEventDao;
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
public final class DatabaseModule_ProvideDiagnosticEventDaoFactory implements Factory<DiagnosticEventDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideDiagnosticEventDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public DiagnosticEventDao get() {
    return provideDiagnosticEventDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideDiagnosticEventDaoFactory create(
      Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideDiagnosticEventDaoFactory(dbProvider);
  }

  public static DiagnosticEventDao provideDiagnosticEventDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideDiagnosticEventDao(db));
  }
}
