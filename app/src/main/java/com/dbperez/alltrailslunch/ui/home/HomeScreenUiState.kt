package com.dbperez.alltrailslunch.ui.home

import com.dbperez.alltrailslunch.domain.model.Location
import com.dbperez.alltrailslunch.domain.model.Place

sealed interface HomeScreenUiState {
    object Loading : HomeScreenUiState
    data class Error(val message: String = "") : HomeScreenUiState
    data class PlaceList(val places: List<Place> = emptyList()) : HomeScreenUiState
    data class PlaceDetails(val place: Place) : HomeScreenUiState
    data class PlaceMap(
        val places: List<Place> = emptyList(),
        val currentLocation: Location
    ) : HomeScreenUiState
}
