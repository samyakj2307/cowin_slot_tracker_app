package com.samyak.cowin_tracker.di

import android.content.Context
import com.samyak.cowin_tracker.presentation.ui.BaseApplication
import com.samyak.cowin_tracker.presentation.util.ConnectionLiveData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }

    @Singleton
    @Provides
    fun randomString(): String {
        return "Hello There I am Samyak fefklsjklsjgd"
    }

    @Singleton
    @Provides
    fun provideConnectionLiveDataClass(@ApplicationContext app: Context):ConnectionLiveData{
        return ConnectionLiveData(app)
    }
}