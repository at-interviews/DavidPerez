package com.dbperez.alltrailslunch.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dbperez.alltrailslunch.R
import com.dbperez.alltrailslunch.domain.model.Location
import com.dbperez.alltrailslunch.domain.model.Place
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.*

@Composable
fun PlaceMap(
    modifier: Modifier = Modifier,
    places: List<Place>,
    currentLocation: Location,
    onMapMarkerClicked: (marker: Marker) -> Unit,
    onMapInfoWindowClosed: () -> Unit,
    onPlaceClicked: (place: Place) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                LatLng(currentLocation.latitude, currentLocation.longitude),
                14f
            )
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false
            )
        ) {
            places.forEach { place ->
                MarkerInfoWindow(
                    state = MarkerState(
                        position = LatLng(
                            place.location.latitude,
                            place.location.longitude
                        )
                    ),
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.map_pin_unselected),
                    onInfoWindowClose = {
                        onMapInfoWindowClosed()
                    },
                    onInfoWindowClick = { onPlaceClicked(place) },
                    onClick = {
                        onMapMarkerClicked(it)
                        false
                    }
                ) {
                    PlaceListItem(
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                        place = place,
                        onPlaceClicked = { onPlaceClicked(it) }
                    )
                }
            }
        }
    }
}
