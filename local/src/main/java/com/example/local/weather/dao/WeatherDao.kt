package com.example.local.weather.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.local.weather.entities.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreCategory(category: List<WeatherEntity>): List<Long>

    @Transaction
    @Query(value = "SELECT * FROM weather  WHERE weather.latitud = :lat AND weather.longitud = :lon")
    fun getWeatherByLatAndLon(lat: Double, lon: Double): Flow<List<WeatherEntity>>

}