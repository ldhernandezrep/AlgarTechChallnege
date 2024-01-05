package com.example.remote.location.response

import com.squareup.moshi.Json

data class GoogleLocationResponse(
    @Json(name = "html_attributions")
    val htmlAttributions: List<Any?>,
    val results: List<Result>,
    val status: String,
)

