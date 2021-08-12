package com.shixin.ui.rxjava;

import com.google.gson.Gson;
import com.shixin.ui.dagger.Car;
import com.shixin.ui.dagger.Poetry;
import dagger.MembersInjector;
import dagger.internal.InjectedFieldSignature;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class DaggerActivity_MembersInjector implements MembersInjector<DaggerActivity> {
  private final Provider<Car> carProvider;

  private final Provider<Poetry> mPoetryProvider;

  private final Provider<Gson> mGsonProvider;

  public DaggerActivity_MembersInjector(Provider<Car> carProvider, Provider<Poetry> mPoetryProvider,
      Provider<Gson> mGsonProvider) {
    this.carProvider = carProvider;
    this.mPoetryProvider = mPoetryProvider;
    this.mGsonProvider = mGsonProvider;
  }

  public static MembersInjector<DaggerActivity> create(Provider<Car> carProvider,
      Provider<Poetry> mPoetryProvider, Provider<Gson> mGsonProvider) {
    return new DaggerActivity_MembersInjector(carProvider, mPoetryProvider, mGsonProvider);
  }

  @Override
  public void injectMembers(DaggerActivity instance) {
    injectCar(instance, carProvider.get());
    injectMPoetry(instance, mPoetryProvider.get());
    injectMGson(instance, mGsonProvider.get());
  }

  @InjectedFieldSignature("com.shixin.ui.rxjava.DaggerActivity.car")
  public static void injectCar(DaggerActivity instance, Car car) {
    instance.car = car;
  }

  @InjectedFieldSignature("com.shixin.ui.rxjava.DaggerActivity.mPoetry")
  public static void injectMPoetry(DaggerActivity instance, Poetry mPoetry) {
    instance.mPoetry = mPoetry;
  }

  @InjectedFieldSignature("com.shixin.ui.rxjava.DaggerActivity.mGson")
  public static void injectMGson(DaggerActivity instance, Gson mGson) {
    instance.mGson = mGson;
  }
}
