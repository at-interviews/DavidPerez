package com.dbperez.alltrailslunch.domain.repository

import com.dbperez.alltrailslunch.common.Resource
import com.dbperez.alltrailslunch.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    fun getCurrentLocation(): Flow<Resource<Location>>
}