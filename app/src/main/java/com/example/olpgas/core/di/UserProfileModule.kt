package com.example.olpgas.core.di

import android.content.SharedPreferences
import com.example.olpgas.user_profile.data.remote.SupabaseUserProfile
import com.example.olpgas.user_profile.data.repository.UserProfileRepositoryImpl
import com.example.olpgas.user_profile.domain.repository.UserProfileRepository
import com.example.olpgas.user_profile.domain.use_case.AddUserUseCase
import com.example.olpgas.user_profile.domain.use_case.GetProfileImageUseCase
import com.example.olpgas.user_profile.domain.use_case.GetUserProfileUseCase
import com.example.olpgas.user_profile.domain.use_case.UpdateAddressUseCase
import com.example.olpgas.user_profile.domain.use_case.UpdateAgeUseCase
import com.example.olpgas.user_profile.domain.use_case.UpdateGenderUseCase
import com.example.olpgas.user_profile.domain.use_case.UpdatePhoneNumberUseCase
import com.example.olpgas.user_profile.domain.use_case.UploadProfileImageUseCase
import com.example.olpgas.user_profile.domain.use_case.UserProfileUsesCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserProfileModule {

    @Provides
    @Singleton
    fun provideSupabaseUserProfile(sharedPreferences: SharedPreferences) : SupabaseUserProfile {
        return SupabaseUserProfile(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideUserProfileRepository(supabaseUserProfile: SupabaseUserProfile) : UserProfileRepository {
        return UserProfileRepositoryImpl(supabaseUserProfile)
    }

    @Provides
    @Singleton
    fun providesUserProfileUseCases(repository: UserProfileRepository) : UserProfileUsesCases {
        return UserProfileUsesCases(
            addUserUseCase = AddUserUseCase(repository),
            getUserProfileUseCase = GetUserProfileUseCase(repository),
            getProfileImageUseCase = GetProfileImageUseCase(repository),
            updateAddressUseCase = UpdateAddressUseCase(repository),
            updateAgeUseCase = UpdateAgeUseCase(repository),
            updateGenderUseCase = UpdateGenderUseCase(repository),
            updatePhoneNumberUseCase = UpdatePhoneNumberUseCase(repository),
            uploadProfileImageUseCase = UploadProfileImageUseCase(repository)
        )
    }
}