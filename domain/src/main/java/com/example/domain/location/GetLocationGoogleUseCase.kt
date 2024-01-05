package com.example.domain.location

import com.example.models.location.LocationModel
import com.example.models.weather.WeatherModel
import com.example.repository.location.LocationGoogleRepositoy
import com.example.repository.weather.WeatherRepositoy
import com.example.utilities.ResultType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface IGetLocationGoogleUseCase{
    suspend fun invoke(query: String, apiKey: String) : Flow<ResultType<List<LocationModel>>>
}

class GetLocationGoogleUseCase @Inject constructor (private val locationGoogleRepositoy: LocationGoogleRepositoy) : IGetLocationGoogleUseCase {
    override suspend fun invoke(
        query: String,
        apiKey: String
    ): Flow<ResultType<List<LocationModel>>> =
        locationGoogleRepositoy.getLocationsByName(query, apiKey)
}