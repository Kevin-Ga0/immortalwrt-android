package com.immortalwrt.manager.core.security;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AndroidKeystoreSecretStore_Factory implements Factory<AndroidKeystoreSecretStore> {
  private final Provider<Context> contextProvider;

  public AndroidKeystoreSecretStore_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AndroidKeystoreSecretStore get() {
    return newInstance(contextProvider.get());
  }

  public static AndroidKeystoreSecretStore_Factory create(Provider<Context> contextProvider) {
    return new AndroidKeystoreSecretStore_Factory(contextProvider);
  }

  public static AndroidKeystoreSecretStore newInstance(Context context) {
    return new AndroidKeystoreSecretStore(context);
  }
}
