package com.dbperez.alltrailslunch.data.remote.dto


import com.dbperez.alltrailslunch.data.remote.API_BASE_URL
import com.dbperez.alltrailslunch.data.remote.API_KEY
import com.dbperez.alltrailslunch.domain.model.Location
import com.dbperez.alltrailslunch.domain.model.Place
import com.squareup.moshi.Json

data class PlaceDto(
    val geometry: GeometryDto,
    val name: String = "",
    val photos: List<PhotoDto>,
    val rating: Double = 0.0,
    @Json(name = "user_ratings_total")
    val userRatingsTotal: Int = 0,
    val vicinity: String = "",
    @Json(name = "formatted_address")
    val formattedAddress: String = "",
    @Json(name = "opening_hours")
    val openingHours: OpeningHoursDto? = null,
    @Json(name = "formatted_phone_number")
    val phoneNumber: String? = null
)

fun PlaceDto.toPlace(): Place {
    val photoUrl = photos.firstOrNull()?.let {
        "${API_BASE_URL}photo?photo_reference=${it.photoReference}&maxwidth=400&key=${API_KEY}"
    }

    return Place(
        name = name,
        rating = rating,
        numReviews = userRatingsTotal,
        address = vicinity.ifEmpty { formattedAddress },
        photoUrl = photoUrl,
        location = Location(geometry.location.lat, geometry.location.lng),
        openNow = openingHours?.openNow ?: false,
        phoneNumber = phoneNumber
    )
}