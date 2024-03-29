package com.example.olpgas.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.olpgas.core.util.Constants
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
    fun provideAuthSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences(
            Constants.AUTH_SHARED_PREF,
            Context.MODE_PRIVATE
        )
    }
}