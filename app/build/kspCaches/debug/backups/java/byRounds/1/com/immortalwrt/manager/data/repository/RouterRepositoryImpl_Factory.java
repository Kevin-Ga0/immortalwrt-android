package com.immortalwrt.manager.data.repository;

import com.immortalwrt.manager.core.network.CertificateFingerprintStore;
import com.immortalwrt.manager.core.network.CertificateTrustManager;
import com.immortalwrt.manager.core.network.EndpointDiscovery;
import com.immortalwrt.manager.core.security.SecretStore;
import com.immortalwrt.manager.data.local.dao.DiagnosticEventDao;
import com.immortalwrt.manager.data.local.dao.RouterCacheDao;
import com.immortalwrt.manager.data.local.dao.RouterCapabilityDao;
import com.immortalwrt.manager.data.local.dao.RouterDao;
import com.immortalwrt.manager.data.local.dao.RouterEnvironmentDao;
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
public final class RouterRepositoryImpl_Factory implements Factory<RouterRepositoryImpl> {
  private final Provider<RouterDao> routerDaoProvider;

  private final Provider<RouterEnvironmentDao> routerEnvironmentDaoProvider;

  private final Provider<RouterCapabilityDao> routerCapabilityDaoProvider;

  private final Provider<RouterCacheDao> routerCacheDaoProvider;

  private final Provider<DiagnosticEventDao> diagnosticEventDaoProvider;

  private final Provider<SecretStore> secretStoreProvider;

  private final Provider<EndpointDiscovery> endpointDiscoveryProvider;

  private final Provider<CertificateTrustManager> certificateTrustManagerProvider;

  private final Provider<CertificateFingerprintStore> certificateFingerprintStoreProvider;

  public RouterRepositoryImpl_Factory(Provider<RouterDao> routerDaoProvider,
      Provider<RouterEnvironmentDao> routerEnvironmentDaoProvider,
      Provider<RouterCapabilityDao> routerCapabilityDaoProvider,
      Provider<RouterCacheDao> routerCacheDaoProvider,
      Provider<DiagnosticEventDao> diagnosticEventDaoProvider,
      Provider<SecretStore> secretStoreProvider,
      Provider<EndpointDiscovery> endpointDiscoveryProvider,
      Provider<CertificateTrustManager> certificateTrustManagerProvider,
      Provider<CertificateFingerprintStore> certificateFingerprintStoreProvider) {
    this.routerDaoProvider = routerDaoProvider;
    this.routerEnvironmentDaoProvider = routerEnvironmentDaoProvider;
    this.routerCapabilityDaoProvider = routerCapabilityDaoProvider;
    this.routerCacheDaoProvider = routerCacheDaoProvider;
    this.diagnosticEventDaoProvider = diagnosticEventDaoProvider;
    this.secretStoreProvider = secretStoreProvider;
    this.endpointDiscoveryProvider = endpointDiscoveryProvider;
    this.certificateTrustManagerProvider = certificateTrustManagerProvider;
    this.certificateFingerprintStoreProvider = certificateFingerprintStoreProvider;
  }

  @Override
  public RouterRepositoryImpl get() {
    return newInstance(routerDaoProvider.get(), routerEnvironmentDaoProvider.get(), routerCapabilityDaoProvider.get(), routerCacheDaoProvider.get(), diagnosticEventDaoProvider.get(), secretStoreProvider.get(), endpointDiscoveryProvider.get(), certificateTrustManagerProvider.get(), certificateFingerprintStoreProvider.get());
  }

  public static RouterRepositoryImpl_Factory create(Provider<RouterDao> routerDaoProvider,
      Provider<RouterEnvironmentDao> routerEnvironmentDaoProvider,
      Provider<RouterCapabilityDao> routerCapabilityDaoProvider,
      Provider<RouterCacheDao> routerCacheDaoProvider,
      Provider<DiagnosticEventDao> diagnosticEventDaoProvider,
      Provider<SecretStore> secretStoreProvider,
      Provider<EndpointDiscovery> endpointDiscoveryProvider,
      Provider<CertificateTrustManager> certificateTrustManagerProvider,
      Provider<CertificateFingerprintStore> certificateFingerprintStoreProvider) {
    return new RouterRepositoryImpl_Factory(routerDaoProvider, routerEnvironmentDaoProvider, routerCapabilityDaoProvider, routerCacheDaoProvider, diagnosticEventDaoProvider, secretStoreProvider, endpointDiscoveryProvider, certificateTrustManagerProvider, certificateFingerprintStoreProvider);
  }

  public static RouterRepositoryImpl newInstance(RouterDao routerDao,
      RouterEnvironmentDao routerEnvironmentDao, RouterCapabilityDao routerCapabilityDao,
      RouterCacheDao routerCacheDao, DiagnosticEventDao diagnosticEventDao, SecretStore secretStore,
      EndpointDiscovery endpointDiscovery, CertificateTrustManager certificateTrustManager,
      CertificateFingerprintStore certificateFingerprintStore) {
    return new RouterRepositoryImpl(routerDao, routerEnvironmentDao, routerCapabilityDao, routerCacheDao, diagnosticEventDao, secretStore, endpointDiscovery, certificateTrustManager, certificateFingerprintStore);
  }
}
