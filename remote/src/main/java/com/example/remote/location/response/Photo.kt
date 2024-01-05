package com.example.remote.location.response

import com.squareup.moshi.Json

data class Photo(
    val height: Long,
    @Json(name ="html_attributions")
    val htmlAttributions: List<String>,
    @Json(name ="photo_reference")
    val photoReference: String,
    val width: Long,
)
