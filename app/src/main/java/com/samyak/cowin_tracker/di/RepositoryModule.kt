package com.samyak.cowin_tracker.di

import com.samyak.cowin_tracker.network.CenterService
import com.samyak.cowin_tracker.network.model.CenterDtoMapper
import com.samyak.cowin_tracker.repository.searchRepository.CenterRepository
import com.samyak.cowin_tracker.repository.searchRepository.CenterRepositoryImpl
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
//
//    @Singleton
//    @Provides
//    fun providePincodeRepository(): PincodeRepository {
//        return PincodeRepository_Impl()
//    }

}