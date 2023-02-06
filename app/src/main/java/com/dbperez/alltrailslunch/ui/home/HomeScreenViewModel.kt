package com.dbperez.alltrailslunch.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbperez.alltrailslunch.R
import com.dbperez.alltrailslunch.common.Resource
import com.dbperez.alltrailslunch.domain.model.Place
import com.dbperez.alltrailslunch.domain.repository.LocationRepository
import com.dbperez.alltrailslunch.domain.repository.PlacesRepository
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val placesRepository: PlacesRepository,
    private val locationProvider: LocationRepository,
) : ViewModel() {

    var state by mutableStateOf<HomeScreenUiState>(HomeScreenUiState.Loading)
        private set

    private var selectedMapMarker: Marker? = null

    init {
        getNearbyPlaces()
    }

    fun getNearbyPlaces() {
        locationProvider.getCurrentLocation()
            .flatMapLatest { locationResource ->
                when (locationResource) {
                    is Resource.Loading -> {
                        flow { emit(Resource.Loading()) }
                    }
                    is Resource.Error -> {
                        flow { emit(Resource.Error(locationResource.message)) }
                    }
                    is Resource.Success -> {
                        locationResource.data?.let {
                            placesRepository.getNearbyPlaces(locationResource.data)
                        } ?: flow { emit(Resource.Error()) }
                    }
                }
            }
            .onEach { updateStateFromPlacesResource(it) }
            .launchIn(viewModelScope)
    }

    fun onSearchSubmitted(searchInput: String) {
        locationProvider.getCurrentLocation()
            .flatMapLatest { locationResource ->
                when (locationResource) {
                    is Resource.Loading -> {
                        flow { emit(Resource.Loading()) }
                    }
                    is Resource.Error -> {
                        flow { emit(Resource.Error(locationResource.message)) }
                    }
                    is Resource.Success -> {
                        locationResource.data?.let {
                            placesRepository.searchForPlace(searchInput, locationResource.data)
                        } ?: flow { emit(Resource.Error()) }
                    }
                }
            }
            .onEach { updateStateFromPlacesResource(it) }
            .launchIn(viewModelScope)
    }

    private fun updateStateFromPlacesResource(placesResource: Resource<List<Place>>) {
        state = when (placesResource) {
            is Resource.Error -> {
                HomeScreenUiState.Error(placesResource.message)
            }
            is Resource.Loading -> {
                HomeScreenUiState.Loading
            }
            is Resource.Success -> {
                HomeScreenUiState.PlaceList(placesResource.data ?: emptyList())
            }
        }
    }

    fun toggleMapListView() {
        when (val targetState = state) {
            is HomeScreenUiState.PlaceList -> {
                locationProvider.getCurrentLocation()
                    .onEach { locationResource ->
                        state = when (locationResource) {
                            is Resource.Error -> {
                                HomeScreenUiState.Error(locationResource.message)
                            }
                            is Resource.Loading -> {
                                HomeScreenUiState.Loading
                            }
                            is Resource.Success -> {
                                if (locationResource.data == null) {
                                    HomeScreenUiState.Error()
                                    return@onEach
                                }

                                HomeScreenUiState.PlaceMap(
                                    places = targetState.places,
                                    currentLocation = locationResource.data
                                )
                            }
                        }
                    }
                    .launchIn(viewModelScope)
            }
            is HomeScreenUiState.PlaceMap -> {
                state = HomeScreenUiState.PlaceList((state as HomeScreenUiState.PlaceMap).places)
            }
            is HomeScreenUiState.PlaceDetails -> getNearbyPlaces()
            else -> Unit
        }
    }

    fun mapMarkerClicked(marker: Marker) {
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_selected))
        selectedMapMarker = marker
    }

    fun onMapInfoWindowClosed() {
        selectedMapMarker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_unselected))
        selectedMapMarker = null
    }

    fun onPlaceClicked(place: Place) {
        state = HomeScreenUiState.PlaceDetails(place)
    }
}
