package com.example.local.location.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.local.location.entities.LocationEntity
import com.example.local.weather.entities.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreCategory(category: List<LocationEntity>): List<Long>

    @Query("SELECT * FROM locations  WHERE locations.name = :name")
    fun getLocationByName(name: String): Flow<List<LocationEntity>>

}