package com.example.olpgas.core.di

import android.app.Application
import com.example.olpgas.core.util.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNetworkConnectivity(app: Application) : NetworkConnectivityObserver {
        return NetworkConnectivityObserver(app)
    }
}