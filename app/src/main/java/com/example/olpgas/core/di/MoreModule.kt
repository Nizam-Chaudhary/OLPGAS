package com.example.olpgas.core.di

import com.example.olpgas.auth.domain.repository.AuthRepository
import com.example.olpgas.more_details.domain.use_case.SignOutUseCase
import com.example.olpgas.user_profile.domain.repository.UserProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MoreModule {

    @Provides
    @Singleton
    fun provideSignOutUseCase(
        authRepository: AuthRepository,
        userProfileRepository: UserProfileRepository
    ) : SignOutUseCase{
        return SignOutUseCase(authRepository, userProfileRepository)
    }
}