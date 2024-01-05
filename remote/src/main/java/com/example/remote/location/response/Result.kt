package com.example.remote.location.response

import com.squareup.moshi.Json

data class Result(
    @Json(name ="formatted_address")
    val formattedAddress: String,
    @Json(name = "business_status")
    @field:Json(name = "business_status")
    val businessStatus: String?,
    val geometry: Geometry,
    val icon: String,
    @Json(name ="icon_background_color")
    val iconBackgroundColor: String,
    @Json(name = "icon_mask_base_uri")
    val iconMaskBaseUri: String,
    val name: String,
    val photos: List<Photo>?,
    @Json(name ="place_id")
    val placeId: String,
    val reference: String,
    val types: List<String>,
    @Json(name ="opening_hours")
    val openingHours: OpeningHours?,
    @Json(name ="plus_code")
    val plusCode: PlusCode?,
    val rating: Double?,
    @Json(name ="user_ratings_total")
    val userRatingsTotal: Long?,
)
