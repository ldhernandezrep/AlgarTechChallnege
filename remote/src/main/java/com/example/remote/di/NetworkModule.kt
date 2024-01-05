package com.example.remote.di

import com.example.remote.weather.api.WeatherApi
import com.example.remote.common.Config
import com.example.remote.location.PlaceGoogleApi
import com.example.remote.location.PlaceGoogleService
import com.example.remote.location.PlaceGoogleServiceImpl
import com.example.remote.weather.WeatherServiceImpl
import com.example.remote.weather.WeathetService
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetWorkModule {


    @Singleton
    @Provides
    @Named("baseUrlWeather")
    fun provideBaseUrlWeather(): String {
        return "https://api.openweathermap.org/data/2.5/"
    }

    @Singleton
    @Provides
    @Named("baseUrlLocations")
    fun provideBaseUrlLocations(): String {
        return "https://maps.googleapis.com/maps/api/"
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(100L, TimeUnit.SECONDS)
            .writeTimeout(100L, TimeUnit.SECONDS)
            .connectTimeout(100L, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    @Named("weatherRetrofit")
    fun provideRetrofit(@Named("baseUrlWeather") baseUrlWeather: String, okHttpClient: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrlWeather)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    @Named("locationRetrofit")
    fun provideLocationRetrofit(@Named("baseUrlLocations") baseUrl: String, okHttpClient: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    fun providePlaceApi(@Named("locationRetrofit") retrofit: Retrofit): PlaceGoogleApi {
        return retrofit.create(PlaceGoogleApi::class.java)
    }

    @Singleton
    @Provides
    fun provideWeatherApi(@Named("weatherRetrofit") retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }

    @Singleton
    @Provides
    fun providerWeatherService(weatherApi: WeatherApi): WeathetService {
        return WeatherServiceImpl(weatherApi)
    }

    @Singleton
    @Provides
    fun providerPlaceService(placeGoogleApi: PlaceGoogleApi): PlaceGoogleService {
        return PlaceGoogleServiceImpl(placeGoogleApi)
    }

}