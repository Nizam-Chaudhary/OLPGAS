package com.example.olpgas.user_profile.data.repository

import com.example.olpgas.user_profile.data.model.UserProfile
import com.example.olpgas.user_profile.data.remote.SupabaseUserProfile
import com.example.olpgas.user_profile.domain.repository.UserProfileRepository

class UserProfileRepositoryImpl(
    private val supabaseUserProfile: SupabaseUserProfile
) : UserProfileRepository {
    override suspend fun getUserProfile(): UserProfile? {
        return supabaseUserProfile.getUserProfile()
    }

    override suspend fun updateGender(gender: String) {
        supabaseUserProfile.updateGender(gender)
    }

    override suspend fun updateAge(age: Int) {
        supabaseUserProfile.updateAge(age)
    }

    override suspend fun updatePhoneNumber(phoneNumber: String) {
        supabaseUserProfile.updatePhoneNumber(phoneNumber)
    }

    override suspend fun updateAddress(streetNumber: String, city: String, state: String) {
        supabaseUserProfile.updateAddress(streetNumber, city, state)
    }

    override suspend fun getProfileImage(): ByteArray? {
        return supabaseUserProfile.getProfileImage()
    }

    override suspend fun uploadProfileImage(imageByteArray: ByteArray) {
        supabaseUserProfile.uploadProfileImage(imageByteArray)
    }

    override suspend fun upsertUser(userProfile: UserProfile) {
        supabaseUserProfile.upsertUser(userProfile)
    }
}