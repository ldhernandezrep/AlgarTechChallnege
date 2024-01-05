package com.example.repository.weather

import com.example.models.weather.WeatherModel
import com.example.utilities.ResultType
import kotlinx.coroutines.flow.Flow

interface WeatherRepositoy {
    fun getWeather(latitud: Double, longitud: Double): Flow<ResultType<WeatherModel>>

}