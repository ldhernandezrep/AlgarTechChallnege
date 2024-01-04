package com.example.remote.common

import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

object MapResultError {

    suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall.invoke()
                return@withContext NetworkResult.NetWorkSuccess(result = response)
            } catch (throwable: Throwable) {
                return@withContext NetworkResult.NetworkFailure<T>(
                    networkError = createError(
                        throwable
                    )
                )
            }
        }
    }

    private fun createError(throwable: Throwable): NetworkError {
        return when (throwable) {
            is IOException -> {
                NetworkError(NetworkErrorType.CONNECTION_ERROR, throwable.message)
            }
            is HttpException -> {
                val bodyResponse: String? = throwable.response()?.errorBody()?.string()
                NetworkError(
                    NetworkErrorType.API_ERROR,
                    bodyResponse,
                    throwable.code().toString(),
                )
            }
            is JsonDataException -> {
                NetworkError(NetworkErrorType.API_ERROR, throwable.message)
            }
            else -> {
                NetworkError(NetworkErrorType.UNKNOWN_ERROR, throwable.message)
            }
        }
    }
}