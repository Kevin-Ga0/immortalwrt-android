package com.immortalwrt.manager.core.network;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class CertificateFingerprintStore_Factory implements Factory<CertificateFingerprintStore> {
  @Override
  public CertificateFingerprintStore get() {
    return newInstance();
  }

  public static CertificateFingerprintStore_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CertificateFingerprintStore newInstance() {
    return new CertificateFingerprintStore();
  }

  private static final class InstanceHolder {
    private static final CertificateFingerprintStore_Factory INSTANCE = new CertificateFingerprintStore_Factory();
  }
}
