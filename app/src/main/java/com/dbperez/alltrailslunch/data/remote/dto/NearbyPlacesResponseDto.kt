package com.dbperez.alltrailslunch.data.remote.dto

data class NearbyPlacesResponseDto(
    val results: List<PlaceDto>,
    val status: String
)