package com.example.remote.common

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class MapResultErrorTest {


    private lateinit var apiCall: suspend () -> String

    @Before
    fun setup() {
        apiCall = mockk()
    }

    @Test
    fun safeApiCall_should_return_when_apiCall_is_successful() {
        // Arrange
        coEvery { apiCall.invoke() } returns "Result"

        // Act
        val result = runBlocking { MapResultError.safeApiCall(apiCall) }

        // Assert
        assertEquals(NetworkResult.NetWorkSuccess("Result"), result)
    }

    @Test
    fun safeApiCall_should_return_networkFailure_with_when_apiCall_throws_IOException() {
        // Arrange
        coEvery { apiCall.invoke() } throws IOException("Connection error")

        // Act
        val result = runBlocking { MapResultError.safeApiCall(apiCall) }

        // Assert
        assertEquals(NetworkResult.NetworkFailure<String>(NetworkError(NetworkErrorType.CONNECTION_ERROR, "Connection error")), result)
    }

    @Test
    fun safeApiCall_should_return_networkFailure_with_API_ERROR_when_apiCall_throws_HttpException() {
        // Arrange
        coEvery { apiCall.invoke() } throws HttpException(Response.error<String>(404, "".toResponseBody()))

        // Act
        val result = runBlocking { MapResultError.safeApiCall(apiCall) }

        // Assert
        assertEquals(NetworkResult.NetworkFailure<String>(NetworkError(NetworkErrorType.API_ERROR, "", "404")), result)
    }

    @Test
    fun safeApiCall_should_return_NetworkFailure_with_UNKNOWN_ERROR_for_other_exceptions() {
        // Arrange
        coEvery { apiCall.invoke() } throws RuntimeException("Some unexpected error")
        // Act
        val result = runBlocking { MapResultError.safeApiCall(apiCall) }
        // Assert
        assertEquals(NetworkResult.NetworkFailure<String>(NetworkError(NetworkErrorType.UNKNOWN_ERROR, "Some unexpected error")), result)
    }
}