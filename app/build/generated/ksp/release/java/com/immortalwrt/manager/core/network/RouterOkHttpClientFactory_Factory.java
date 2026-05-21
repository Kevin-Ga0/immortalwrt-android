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
public final class RouterOkHttpClientFactory_Factory implements Factory<RouterOkHttpClientFactory> {
  private final Provider<CertificateTrustManager> certificateTrustManagerProvider;

  private final Provider<CertificateFingerprintStore> fingerprintStoreProvider;

  public RouterOkHttpClientFactory_Factory(
      Provider<CertificateTrustManager> certificateTrustManagerProvider,
      Provider<CertificateFingerprintStore> fingerprintStoreProvider) {
    this.certificateTrustManagerProvider = certificateTrustManagerProvider;
    this.fingerprintStoreProvider = fingerprintStoreProvider;
  }

  @Override
  public RouterOkHttpClientFactory get() {
    return newInstance(certificateTrustManagerProvider.get(), fingerprintStoreProvider.get());
  }

  public static RouterOkHttpClientFactory_Factory create(
      Provider<CertificateTrustManager> certificateTrustManagerProvider,
      Provider<CertificateFingerprintStore> fingerprintStoreProvider) {
    return new RouterOkHttpClientFactory_Factory(certificateTrustManagerProvider, fingerprintStoreProvider);
  }

  public static RouterOkHttpClientFactory newInstance(
      CertificateTrustManager certificateTrustManager,
      CertificateFingerprintStore fingerprintStore) {
    return new RouterOkHttpClientFactory(certificateTrustManager, fingerprintStore);
  }
}
