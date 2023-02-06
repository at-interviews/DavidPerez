package com.dbperez.alltrailslunch.ui.di

import com.dbperez.alltrailslunch.data.repository.DefaultLocationRepository
import com.dbperez.alltrailslunch.data.repository.DefaultPlacesRepository
import com.dbperez.alltrailslunch.domain.repository.LocationRepository
import com.dbperez.alltrailslunch.domain.repository.PlacesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UiModule {

    @Binds
    @Singleton
    abstract fun bindPlacesRepository(placesRepository: DefaultPlacesRepository): PlacesRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(locationRepository: DefaultLocationRepository): LocationRepository
}
