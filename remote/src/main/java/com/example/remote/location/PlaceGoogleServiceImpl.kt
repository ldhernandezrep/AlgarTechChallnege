package com.example.remote.location

import com.example.remote.common.MapResultError
import com.example.remote.common.NetworkResult
import com.example.remote.location.response.GoogleLocationResponse
import com.example.remote.weather.WeathetService
import com.example.remote.weather.api.WeatherApi
import com.example.remote.weather.response.Root
import javax.inject.Inject

class PlaceGoogleServiceImpl @Inject constructor(val placeGoogleApi: PlaceGoogleApi) :
    PlaceGoogleService {

    override suspend fun getLocationByQuery(
        query: String,
        apikey: String
    ): NetworkResult<GoogleLocationResponse> =
        MapResultError.safeApiCall {
            placeGoogleApi.searchPlaces(query, apikey)

        }
}