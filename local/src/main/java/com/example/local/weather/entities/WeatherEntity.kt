package com.example.local.weather.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "weathers"
)
data class WeatherEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val latitud: Double,
    val longitud: Double,
    val main: String,
    val temp: Double,
    val name: String,
)
