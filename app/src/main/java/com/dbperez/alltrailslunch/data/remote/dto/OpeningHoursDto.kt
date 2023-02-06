package com.dbperez.alltrailslunch.data.remote.dto

import com.squareup.moshi.Json

data class OpeningHoursDto(
    @Json(name = "open_now")
    val openNow: Boolean? = null
)
