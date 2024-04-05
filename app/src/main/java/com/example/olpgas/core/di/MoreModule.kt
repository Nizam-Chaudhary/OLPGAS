package com.example.olpgas.core.di

import android.app.Application
import com.example.olpgas.auth.domain.repository.AuthRepository
import com.example.olpgas.more_details.data.local.SettingsSharedPreferences
import com.example.olpgas.more_details.data.repository.MoreRepositoryImpl
import com.example.olpgas.more_details.domain.repository.MoreRepository
import com.example.olpgas.more_details.domain.use_case.ChangeThemeModeUseCase
import com.example.olpgas.more_details.domain.use_case.GetThemeModeUseCase
import com.example.olpgas.more_details.domain.use_case.GetUserNameUseCase
import com.example.olpgas.more_details.domain.use_case.GetUserProfileImageUseCase
import com.example.olpgas.more_details.domain.use_case.MoreUseCases
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
    fun provideSettingsSharedPreferences(application: Application) :
            SettingsSharedPreferences {
        return SettingsSharedPreferences(application)
    }

    @Provides
    @Singleton
    fun provideMoreRepository(settingsSharedPreferences: SettingsSharedPreferences) :
            MoreRepository {
        return MoreRepositoryImpl(settingsSharedPreferences)
    }

    @Provides
    @Singleton
    fun provideMoreUseCases(
        authRepository: AuthRepository,
        userProfileRepository: UserProfileRepository,
        moreRepository: MoreRepository
    ) : MoreUseCases {
        return MoreUseCases(
            GetUserNameUseCase(userProfileRepository),
            GetUserProfileImageUseCase(userProfileRepository),
            SignOutUseCase(authRepository, userProfileRepository),
            GetThemeModeUseCase(moreRepository),
            ChangeThemeModeUseCase(moreRepository)
        )
    }
}