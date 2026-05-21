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
public final class UbusCallIdGenerator_Factory implements Factory<UbusCallIdGenerator> {
  @Override
  public UbusCallIdGenerator get() {
    return newInstance();
  }

  public static UbusCallIdGenerator_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static UbusCallIdGenerator newInstance() {
    return new UbusCallIdGenerator();
  }

  private static final class InstanceHolder {
    private static final UbusCallIdGenerator_Factory INSTANCE = new UbusCallIdGenerator_Factory();
  }
}
