package com.example.domain.di

import com.example.domain.location.GetLocationGoogleUseCase
import com.example.domain.location.IGetLocationGoogleUseCase
import com.example.domain.weather.usecases.GetWeatherByGeoUseCase
import com.example.domain.weather.usecases.IGetWeatherByGeoUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {

    @Binds
    fun providerWeatherUseCase(weatherByGeoUseCase: GetWeatherByGeoUseCase) : IGetWeatherByGeoUseCase

    @Binds
    fun providerGetLocationGoogleUseCase(getLocationGoogleUseCase: GetLocationGoogleUseCase) : IGetLocationGoogleUseCase


}