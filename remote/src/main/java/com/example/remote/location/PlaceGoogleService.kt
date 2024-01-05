package com.example.remote.location

import com.example.remote.common.NetworkResult
import com.example.remote.location.response.GoogleLocationResponse
import com.example.remote.weather.response.Root

interface PlaceGoogleService {
    suspend fun getLocationByQuery(query: String,apikey: String): NetworkResult<GoogleLocationResponse>
}