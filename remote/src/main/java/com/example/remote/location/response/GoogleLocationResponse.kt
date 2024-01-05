package com.example.remote.location.response

import com.squareup.moshi.Json

data class GoogleLocationResponse(
    @Json(name = "html_attributions")
    val htmlAttributions: List<Any?>,
    @Json(name = "next_page_token")
    @field:Json(name = "next_page_token")
    val nextPageToken: String?,
    val results: List<Result>,
    val status: String,
)