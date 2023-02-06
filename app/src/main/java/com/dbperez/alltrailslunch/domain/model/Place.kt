package com.dbperez.alltrailslunch.domain.model

data class Place(
    val name: String,
    val rating: Double,
    val numReviews: Int,
    val address: String,
    val photoUrl: String? = null,
    val location: Location,
    val openNow: Boolean,
    val phoneNumber: String? = null
)
