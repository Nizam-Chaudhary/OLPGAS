package com.example.olpgas.core.di

import android.content.SharedPreferences
import com.example.olpgas.auth.data.remote.SupabaseAuth
import com.example.olpgas.auth.data.repository.AuthRepositoryImpl
import com.example.olpgas.auth.domain.repository.AuthRepository
import com.example.olpgas.auth.domain.use_case.GoogleSignInUseCase
import com.example.olpgas.auth.domain.use_case.LoginUseCase
import com.example.olpgas.auth.domain.use_case.SignupUseCase
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
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSignupUseCase(repository: AuthRepository): SignupUseCase {
        return SignupUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGoogleSignInUseCase(repository: AuthRepository): GoogleSignInUseCase {
        return GoogleSignInUseCase(repository)
    }
}