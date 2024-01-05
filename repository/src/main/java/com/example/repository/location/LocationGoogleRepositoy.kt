package com.example.repository.location

import com.example.models.location.LocationModel
import com.example.models.weather.WeatherModel
import com.example.utilities.ResultType
import kotlinx.coroutines.flow.Flow

interface LocationGoogleRepositoy {
    fun getLocationsByName(query: String, apiKey: String): Flow<ResultType<List<LocationModel>>>
    suspend fun saveLocations(items: List<LocationModel>): List<Long>
}