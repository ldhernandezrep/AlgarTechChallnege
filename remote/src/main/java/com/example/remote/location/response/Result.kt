package com.example.remote.location.response

import com.squareup.moshi.Json

data class Result(
    @Json(name ="formatted_address")
    val formattedAddress: String,
    val geometry: Geometry,
    val icon: String,
    @Json(name ="icon_background_color")
    val iconBackgroundColor: String,
    @Json(name = "icon_mask_base_uri")
    val iconMaskBaseUri: String,
    val name: String,
    val photos: List<Photo>,
    @Json(name ="place_id")
    val placeId: String,
    val reference: String,
    val types: List<String>,
)
