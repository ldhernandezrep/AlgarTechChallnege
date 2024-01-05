package com.example.remote.weather

import com.example.remote.weather.api.WeatherApi
import com.example.remote.common.MapResultError.safeApiCall
import com.example.remote.common.NetworkResult
import com.example.remote.weather.response.Root
import javax.inject.Inject

class WeatherServiceImpl @Inject constructor(val weatherApi: WeatherApi) : WeathetService {

    override suspend fun getWeatherByLatAndLon(latitud: Double,longitud: Double): NetworkResult<Root> =
        safeApiCall{
            weatherApi.weather(latitud, longitud)
        }

}