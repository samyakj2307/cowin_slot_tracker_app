package com.samyak.retrofitapp.di

import com.google.gson.GsonBuilder
import com.samyak.retrofitapp.network.CenterService
import com.samyak.retrofitapp.network.model.CenterDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Singleton
    @Provides
    fun provideCenterMapper():CenterDtoMapper{
        return CenterDtoMapper()
    }

    @Singleton
    @Provides
    fun provideCenterService():CenterService{
        return Retrofit.Builder()
            .baseUrl("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(CenterService::class.java)
    }
}