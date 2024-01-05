package com.example.repository.weather

import com.example.local.weather.entities.WeatherEntity
import com.example.models.location.LocationModel
import com.example.models.weather.WeatherModel
import com.example.utilities.ResultType
import kotlinx.coroutines.flow.Flow

interface WeatherRepositoy {
    fun getWeather(latitud: Double, longitud: Double, appid: String): Flow<ResultType<WeatherModel>>
    suspend fun saveWeather(items: WeatherModel): Long

}