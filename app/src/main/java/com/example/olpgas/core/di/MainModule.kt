package com.example.olpgas.core.di

import com.example.olpgas.auth.domain.repository.AuthRepository
import com.example.olpgas.main_activity.domain.use_case.UserLoggedInStatus
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideUserLoggedInStatus(repository: AuthRepository) : UserLoggedInStatus {
        return UserLoggedInStatus(repository)
    }
}