package com.example.repository.location

import com.example.local.location.dao.LocationDao
import com.example.models.location.LocationModel
import com.example.remote.common.NetworkError
import com.example.remote.common.NetworkErrorType
import com.example.remote.common.NetworkResult
import com.example.remote.location.PlaceGoogleService
import com.example.remote.location.response.Geometry
import com.example.remote.location.response.GoogleLocationResponse
import com.example.remote.location.response.Location
import com.example.remote.location.response.Northeast
import com.example.remote.location.response.OpeningHours
import com.example.remote.location.response.Photo
import com.example.remote.location.response.PlusCode
import com.example.remote.location.response.Result
import com.example.remote.location.response.Southwest
import com.example.remote.location.response.Viewport
import com.example.repository.location.mappers.toEntity
import com.example.repository.location.mappers.toModel
import com.example.utilities.ResultType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LocationGoogleRepositoryImplTest {

    private lateinit var mockLocationDao: LocationDao
    private lateinit var mockPlaceGoogleService: PlaceGoogleService
    private lateinit var locationRepository: LocationGoogleRepositoryImpl

    @Before
    fun setUp() {
        mockLocationDao = mockk()
        mockPlaceGoogleService = mockk()
        locationRepository = LocationGoogleRepositoryImpl(mockLocationDao,mockPlaceGoogleService)
    }

    @Test
    fun `getLocationsByName should return success result when remote call is successful`() = runBlocking {
        // Arrange
        val query = "some_query"
        val apiKey = "some_api_key"
        val locationModel = LocationModel(19.8039297,-99.0930528,"Zumpango")
        val simulatedResponse = GoogleLocationResponse(
            htmlAttributions = emptyList(),
            results = listOf(
                Result(
                    businessStatus = "",
                    formattedAddress = "Zumpango",
                    geometry = Geometry(
                        location = Location(lat = 19.8039297, lng = -99.0930528),
                        viewport = Viewport(
                            northeast = Northeast(lat = 19.823695, lng = -99.05929379999999),
                            southwest = Southwest(lat = 19.7719895, lng = -99.1193428)
                        )
                    ),
                    plusCode = PlusCode("",""),
                    openingHours = OpeningHours(true),
                    rating = 12.67,
                    userRatingsTotal = 14,
                    icon = "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/geocode-71.png",
                    iconBackgroundColor = "#7B9EB0",
                    iconMaskBaseUri = "https://maps.gstatic.com/mapfiles/place_api/icons/v2/generic_pinlet",
                    name = "Zumpango de Ocampo",
                    photos = listOf(
                        Photo(
                            height = 4000,
                            htmlAttributions = listOf("<a href=\"https://maps.google.com/maps/contrib/100097659350764250846\">A Google User</a>"),
                            photoReference = "AWU5eFgEhCKvprOHqvcxl2FokV_bNOmI8g0rCYGAcKbpF11JESZJzvmAeUWwlUxz2cGfhpsS3_b4lrHalYtzK9BDi_DhQSRwx2A0N3GlaQ2tJ7NaVOTP9n0E0m6uUzUQOTjOxpC168cH7GonVI_U1kRUf4q_bb-S2lNXAHivgQaHarjGsFSX",
                            width = 3000
                        )
                    ),
                    placeId = "ChIJb5C5mQuM0YURF6ULyR0AmCA",
                    reference = "ChIJb5C5mQuM0YURF6ULyR0AmCA",
                    types = listOf("locality", "political")
                )
            ),
            status = "200",
            nextPageToken = ""
        )
        val networkResult = NetworkResult.NetWorkSuccess(result = simulatedResponse)
        val ids:List<Long> = listOf()

        coEvery { mockPlaceGoogleService.getLocationByQuery(query, apiKey) } returns networkResult
        coEvery { mockLocationDao.insertOrIgnoreCategory(any()) } returns ids
        // Act
        val result = locationRepository.getLocationsByName(query, apiKey)

        // Assert
        assertEquals(ResultType.Success(data = listOf(locationModel)), result.single())
    }

    @Test
    fun `getLocationsByName should return success result when remote call fails and local data is available`() =
        runBlocking {
            // Arrange
            val query = "some_query"
            val apiKey = "some_api_key"
            val locationModel = LocationModel(-99.34,15.89,"Zumpango")

            val localResults = listOf(locationModel)
            coEvery { mockLocationDao.getLocationByName(query) } returns flow { emit(localResults.map { it.toEntity() }) }

            val networkResult = NetworkResult.NetworkFailure<NetworkError>(networkError = NetworkError(NetworkErrorType.CONNECTION_ERROR,"Error desconocido",""))
            coEvery { mockPlaceGoogleService.getLocationByQuery(query, apiKey) } returns networkResult as NetworkResult<GoogleLocationResponse>

            // Act
            val result = locationRepository.getLocationsByName(query, apiKey)

            // Assert
            assertEquals(ResultType.Success(data = localResults), result.single())
        }

    @Test
    fun `getLocationsByName should return error result when remote call fails and no local data is available`() =
        runBlocking {
            // Arrange
            val query = "some_query"
            val apiKey = "some_api_key"

            val networkResult = NetworkResult.NetworkFailure<NetworkError>(networkError = NetworkError(NetworkErrorType.UNKNOWN_ERROR,"Error de red: UNKNOWN_ERROR",""))
            coEvery { mockPlaceGoogleService.getLocationByQuery(query, apiKey) } returns networkResult as NetworkResult<GoogleLocationResponse>

            // Act
            val result = locationRepository.getLocationsByName(query, apiKey)

            // Assert
            assertEquals(ResultType.Error(message = "Error desconocido"), result.single())
        }
}