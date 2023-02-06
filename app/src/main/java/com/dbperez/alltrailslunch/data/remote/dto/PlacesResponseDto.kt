package com.dbperez.alltrailslunch.data.remote.dto

data class PlacesResponseDto(
    val results: List<PlaceDto>,
    val status: String
)