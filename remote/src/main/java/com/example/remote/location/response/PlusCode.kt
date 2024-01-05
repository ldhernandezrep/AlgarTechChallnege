package com.example.remote.location.response

import com.squareup.moshi.Json

data class PlusCode(
    @Json(name ="compound_code")
    val compoundCode: String,
    @Json(name ="global_code")
    val globalCode: String,
)