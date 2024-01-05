package com.example.local.weather.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.local.weather.entities.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreWheater(weather: WeatherEntity): Long

    @Query("SELECT * FROM weathers WHERE weathers.latitud = :lat AND weathers.longitud = :lon LIMIT 1")
    fun getWeatherByLatAndLon(lat: Double, lon: Double): Flow<WeatherEntity>

}