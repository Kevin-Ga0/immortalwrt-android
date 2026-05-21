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
public final class EndpointDiscovery_Factory implements Factory<EndpointDiscovery> {
  private final Provider<NetworkMonitor> networkMonitorProvider;

  public EndpointDiscovery_Factory(Provider<NetworkMonitor> networkMonitorProvider) {
    this.networkMonitorProvider = networkMonitorProvider;
  }

  @Override
  public EndpointDiscovery get() {
    return newInstance(networkMonitorProvider.get());
  }

  public static EndpointDiscovery_Factory create(Provider<NetworkMonitor> networkMonitorProvider) {
    return new EndpointDiscovery_Factory(networkMonitorProvider);
  }

  public static EndpointDiscovery newInstance(NetworkMonitor networkMonitor) {
    return new EndpointDiscovery(networkMonitor);
  }
}
