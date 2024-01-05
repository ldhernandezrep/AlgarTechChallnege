package com.example.remote.location

import com.example.remote.common.NetworkError
import com.example.remote.common.NetworkErrorType
import com.example.remote.common.NetworkResult
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
import com.squareup.moshi.JsonDataException
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class PlaceGoogleServiceImplTest {


    private lateinit var placeGoogleApi: PlaceGoogleApi
    private lateinit var placeGoogleService: PlaceGoogleServiceImpl
    @Before
    fun setup() {
        placeGoogleApi = mockk()
        placeGoogleService = PlaceGoogleServiceImpl(placeGoogleApi)
    }

    @Test
    fun getLocationByQueryByRetrunNetworkResultNetWorkSuccess() = runBlocking {
        val simulatedResponse = GoogleLocationResponse(
            htmlAttributions = emptyList(),
            results = listOf(
                Result(
                    businessStatus = "",
                    formattedAddress = "Zumpango de Ocampo, Méx., México",
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
        coEvery { placeGoogleApi.searchPlaces("Zumpango", "Uno") } returns simulatedResponse
        val result = placeGoogleService.getLocationByQuery("Zumpango", "Uno")

        assertTrue(result is NetworkResult.NetWorkSuccess)
        assertEquals(simulatedResponse, (result as NetworkResult.NetWorkSuccess).result)
    }

    @Test
    fun `getLocation_By_Query_By_Return_Network_API_ERROR`() = runBlocking {
        coEvery { placeGoogleApi.searchPlaces("Zumpango", "Uno") } throws JsonDataException("Error de mapeo")
        // Act
        val result = placeGoogleService.getLocationByQuery("Zumpango", "Uno")

        // Assert
        assertEquals(NetworkResult.NetworkFailure<NetworkError>(
            networkError = NetworkError(
                NetworkErrorType.API_ERROR,
                "Error de mapeo"
            )
        ), result)

    }

    @Test
    fun `getLocationByQueryByReturnNetworkCONNECTION_ERROR`() = runBlocking {
        coEvery { placeGoogleApi.searchPlaces("Zumpango", "Uno") } throws IOException("Error de red")
        // Act
        val result = placeGoogleService.getLocationByQuery("Zumpango", "Uno")

        // Assert
        assertEquals(NetworkResult.NetworkFailure<NetworkError>(
            networkError = NetworkError(
                NetworkErrorType.CONNECTION_ERROR,
                "Error de red"
            )
        ), result)

    }

    @Test
    fun `getLocation_By_Query_By_Return_Network_UNKNOWN_ERROR`() = runBlocking {
        // Mock the behavior of the PlaceGoogleApi to throw an exception
        coEvery { placeGoogleApi.searchPlaces("Zumpango", "Uno") } throws RuntimeException("Error desconocido")
        // Act
        val result = placeGoogleService.getLocationByQuery("Zumpango", "Uno")

        // Assert
        assertEquals(NetworkResult.NetworkFailure<NetworkError>(
            networkError = NetworkError(
                NetworkErrorType.UNKNOWN_ERROR,
                "Error desconocido"
            )
        ), result)

    }

}