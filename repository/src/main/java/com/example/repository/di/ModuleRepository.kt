package com.example.repository.di

import com.example.repository.location.LocationGoogleRepositoryImpl
import com.example.repository.location.LocationGoogleRepositoy
import com.example.repository.weather.WeatherRepositoryImplement
import com.example.repository.weather.WeatherRepositoy
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ModuleRepository{

    @Binds
    fun providerWeatherRepository(weatherRepositoryImplement: WeatherRepositoryImplement): WeatherRepositoy

    @Binds
    fun providerLocationRepository(locationGoogleRepositoryImpl: LocationGoogleRepositoryImpl): LocationGoogleRepositoy


}