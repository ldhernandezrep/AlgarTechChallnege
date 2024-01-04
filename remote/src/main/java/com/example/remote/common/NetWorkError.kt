package com.example.remote.common

enum class NetworkErrorType {
    CONNECTION_ERROR,
    API_ERROR,
    UNKNOWN_ERROR
}

data class NetworkError(
    var type: NetworkErrorType,
    var rawError: String? = null,
    var errorCode: String? = null
)