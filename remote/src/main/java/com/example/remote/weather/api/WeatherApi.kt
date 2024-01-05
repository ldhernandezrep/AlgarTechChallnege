package com.example.remote.weather.api

import com.example.remote.weather.response.Root
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather/")
    suspend fun weather(
        @Query("lat") latitud: Double,
        @Query("lon") longitud: Double,
        @Query("appid") appid: String
    ): Root

}