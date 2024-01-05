package com.example.remote.location

import com.example.remote.location.response.GoogleLocationResponse
import com.example.remote.weather.response.Root
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceGoogleApi {
    @GET("place/textsearch/json")
    suspend fun searchPlaces(
        @Query("query") query: String,
        @Query("key") apiKey: String
    ) : GoogleLocationResponse

}