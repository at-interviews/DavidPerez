package com.dbperez.alltrailslunch.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.dbperez.alltrailslunch.R
import com.dbperez.alltrailslunch.common.Resource
import com.dbperez.alltrailslunch.domain.model.Location
import com.dbperez.alltrailslunch.domain.repository.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DefaultLocationRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val locationService: FusedLocationProviderClient,
) : LocationRepository {

    override fun getCurrentLocation(): Flow<Resource<Location>> {
        return flow {
            emit(Resource.Loading())

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                emit(Resource.Error(context.getString(R.string.location_permission_not_granted)))
                return@flow
            }

            val location = locationService.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(listener: OnTokenCanceledListener) =
                        CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                }).await()

            if (location == null) {
                emit(Resource.Error(context.getString(R.string.generic_location_error)))
                return@flow
            }

            emit(Resource.Success(Location(location.latitude, location.longitude)))
        }
    }
}
