package com.dbperez.alltrailslunch.data.repository

import android.content.Context
import com.dbperez.alltrailslunch.R
import com.dbperez.alltrailslunch.common.Resource
import com.dbperez.alltrailslunch.data.remote.GooglePlacesApi
import com.dbperez.alltrailslunch.data.remote.dto.toPlace
import com.dbperez.alltrailslunch.domain.model.Location
import com.dbperez.alltrailslunch.domain.model.Place
import com.dbperez.alltrailslunch.domain.repository.PlacesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultPlacesRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val placesApi: GooglePlacesApi,
) : PlacesRepository {

    override fun getNearbyPlaces(location: Location): Flow<Resource<List<Place>>> {
        return flow {
            try {
                emit(Resource.Loading())
                val nearbyPlaces =
                    placesApi.getNearbyPlaces(location = "${location.latitude},${location.longitude}")
                        .results.map {
                            it.toPlace()
                        }
                emit(Resource.Success(nearbyPlaces))
            } catch (exception: Exception) {
                emit(Resource.Error(context.getString(R.string.nearby_places_request_error)))
            }
        }
    }

    override fun searchForPlace(
        searchInput: String,
        location: Location
    ): Flow<Resource<List<Place>>> {
        return flow {
            try {
                emit(Resource.Loading())
                val nearbyPlaces =
                    placesApi.searchForPlace(
                        searchInput = searchInput,
                        location = "${location.latitude},${location.longitude}"
                    )
                        .results.map {
                            it.toPlace()
                        }
                emit(Resource.Success(nearbyPlaces))
            } catch (exception: Exception) {
                emit(Resource.Error(context.getString(R.string.nearby_places_request_error)))
            }
        }
    }
}