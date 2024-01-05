package com.example.local.di

import com.example.local.AlgarTechDatabase
import com.example.local.location.dao.LocationDao
import com.example.local.weather.dao.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    fun providesWeatherDao(
        database: AlgarTechDatabase,
    ): WeatherDao = database.wheatherDao()

    @Provides
    fun providesLocationDao(
        database: AlgarTechDatabase,
    ): LocationDao = database.locationDao()
}