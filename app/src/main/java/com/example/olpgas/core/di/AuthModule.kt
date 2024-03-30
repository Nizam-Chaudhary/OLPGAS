package com.example.olpgas.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.olpgas.auth.data.remote.SupabaseAuth
import com.example.olpgas.auth.data.repository.AuthRepositoryImpl
import com.example.olpgas.auth.domain.repository.AuthRepository
import com.example.olpgas.auth.domain.use_case.GoogleSignInUseCase
import com.example.olpgas.auth.domain.use_case.LoginUseCase
import com.example.olpgas.auth.domain.use_case.LoginUseCases
import com.example.olpgas.auth.domain.use_case.SetUpUserUseCase
import com.example.olpgas.auth.domain.use_case.SetUpUserWithGoogleUseCase
import com.example.olpgas.auth.domain.use_case.SetUserProfileLocalCacheUseCase
import com.example.olpgas.auth.domain.use_case.SignupUseCase
import com.example.olpgas.core.util.Constants
import com.example.olpgas.user_profile.domain.repository.UserProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences(
            Constants.AUTH_SHARED_PREF,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun provideSupabaseAuth(authSharedPreferences: SharedPreferences): SupabaseAuth {
        return SupabaseAuth(authSharedPreferences)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(supabaseAuth: SupabaseAuth): AuthRepository {
        return AuthRepositoryImpl(supabaseAuth)
    }


    @Provides
    @Singleton
    fun provideSignupUseCase(repository: AuthRepository): SignupUseCase {
        return SignupUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLoginUseCases(authRepository: AuthRepository, userProfileRepository: UserProfileRepository) : LoginUseCases {
        return LoginUseCases(
            LoginUseCase(authRepository),
            GoogleSignInUseCase(authRepository),
            SetUserProfileLocalCacheUseCase(userProfileRepository),
            SetUpUserUseCase(userProfileRepository),
            SetUpUserWithGoogleUseCase(userProfileRepository)
        )
    }
}