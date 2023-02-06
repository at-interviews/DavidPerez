package com.dbperez.alltrailslunch.data.remote

import com.dbperez.alltrailslunch.data.remote.dto.NearbyPlacesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "AIzaSyCqWHKkgLxJiSwS63bxfWpQ-XhSQs65H5c"
const val API_BASE_URL = "https://maps.googleapis.com/maps/api/place/"

interface GooglePlacesApi {

    @GET("nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int = 1500,
        @Query("type") type: String = "restaurant",
        @Query("key") key: String = API_KEY
    ): NearbyPlacesResponseDto

    @GET("textsearch/json")
    suspend fun searchForPlace(
        @Query("query") searchInput: String,
        @Query("location") location: String,
        @Query("radius") radius: Int = 1500,
        @Query("type") type: String = "restaurant",
        @Query("key") key: String = API_KEY
    ): NearbyPlacesResponseDto
}
