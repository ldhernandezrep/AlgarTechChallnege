package com.example.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.local.location.dao.LocationDao
import com.example.local.weather.dao.WeatherDao
import com.example.local.weather.entities.WeatherEntity

@Database(
    entities = [
        WeatherEntity::class,
    ],
    version = 1
)
abstract class AlgarTechDatabase : RoomDatabase() {

        abstract fun wheatherDao(): WeatherDao
    abstract fun locationDao(): LocationDao

}