package com.example.remote.location.response

import com.squareup.moshi.Json

data class OpeningHours(
    @Json(name ="open_now")
    val openNow: Boolean,
)
