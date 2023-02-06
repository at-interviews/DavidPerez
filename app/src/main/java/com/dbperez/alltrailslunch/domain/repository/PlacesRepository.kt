package com.dbperez.alltrailslunch.domain.repository

import com.dbperez.alltrailslunch.common.Resource
import com.dbperez.alltrailslunch.domain.model.Location
import com.dbperez.alltrailslunch.domain.model.Place
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {

    fun getNearbyPlaces(location: Location): Flow<Resource<List<Place>>>
    fun searchForPlace(searchInput: String, location: Location): Flow<Resource<List<Place>>>
}