package com.dbperez.alltrailslunch.data.remote.dto

import com.squareup.moshi.Json

data class PhotoDto(
    val height: Int,
    @Json(name = "html_attributions")
    val htmlAttributions: List<String>,
    @Json(name = "photo_reference")
    val photoReference: String,
    val width: Int
)