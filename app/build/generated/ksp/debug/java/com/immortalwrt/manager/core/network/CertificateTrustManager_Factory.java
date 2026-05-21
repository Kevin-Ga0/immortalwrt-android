package com.immortalwrt.manager.core.network;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class CertificateTrustManager_Factory implements Factory<CertificateTrustManager> {
  private final Provider<CertificateFingerprintStore> fingerprintStoreProvider;

  public CertificateTrustManager_Factory(
      Provider<CertificateFingerprintStore> fingerprintStoreProvider) {
    this.fingerprintStoreProvider = fingerprintStoreProvider;
  }

  @Override
  public CertificateTrustManager get() {
    return newInstance(fingerprintStoreProvider.get());
  }

  public static CertificateTrustManager_Factory create(
      Provider<CertificateFingerprintStore> fingerprintStoreProvider) {
    return new CertificateTrustManager_Factory(fingerprintStoreProvider);
  }

  public static CertificateTrustManager newInstance(CertificateFingerprintStore fingerprintStore) {
    return new CertificateTrustManager(fingerprintStore);
  }
}
