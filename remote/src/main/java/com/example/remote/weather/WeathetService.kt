package com.example.remote.weather

import com.example.remote.common.NetworkResult
import com.example.remote.weather.response.Root

interface WeathetService {

    suspend fun getWeatherByLatAndLon(latitud: Double,longitud: Double, appid: String): NetworkResult<Root>
}