package com.example.repository.common

sealed class ResultType<out T> {
    data class Success<out T>(val data: T) : ResultType<T>()
    data class Error(val message: String) : ResultType<Nothing>()
}
