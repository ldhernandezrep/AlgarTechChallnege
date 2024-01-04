package com.example.remote.weather.response

import com.squareup.moshi.Json

data class Main(
    val temp: Double,
    @Json(name = "feels_like")
    val feelsLike: Double,
    @Json(name ="temp_min")
    val tempMin: Double,
    @Json(name ="temp_max")
    val tempMax: Double,
    val pressure: Long,
    val humidity: Long,
)
