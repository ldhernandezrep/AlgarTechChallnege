package com.example.remote.location

import com.example.remote.common.MapResultError.safeApiCall
import com.example.remote.common.NetworkResult
import com.example.remote.location.response.GoogleLocationResponse
import javax.inject.Inject

class PlaceGoogleServiceImpl @Inject constructor(val placeGoogleApi: PlaceGoogleApi) :
    PlaceGoogleService {

    override suspend fun getLocationByQuery(
        query: String,
        apikey: String
    ): NetworkResult<GoogleLocationResponse> =
        safeApiCall {
            placeGoogleApi.searchPlaces(query, apikey)

        }
}