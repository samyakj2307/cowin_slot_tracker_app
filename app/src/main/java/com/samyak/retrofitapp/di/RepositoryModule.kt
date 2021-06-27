package com.samyak.retrofitapp.di

import com.samyak.retrofitapp.network.CenterService
import com.samyak.retrofitapp.network.model.CenterDtoMapper
import com.samyak.retrofitapp.repository.CenterRepository
import com.samyak.retrofitapp.repository.CenterRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideCenterRepository(
        centerService: CenterService,
        centerDtoMapper: CenterDtoMapper
    ): CenterRepository {
        return CenterRepositoryImpl(centerService, centerDtoMapper)
    }

}