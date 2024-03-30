package com.example.olpgas.user_profile.data.repository

import android.util.Log
import com.example.olpgas.user_profile.data.local.ProfileImageLocalStorage
import com.example.olpgas.user_profile.data.local.UserProfileSharedPreferences
import com.example.olpgas.user_profile.data.model.UserProfile
import com.example.olpgas.user_profile.data.remote.SupabaseUserProfile
import com.example.olpgas.user_profile.domain.repository.UserProfileRepository

class UserProfileRepositoryImpl(
    private val supabaseUserProfile: SupabaseUserProfile,
    private val userProfileSharedPreferences: UserProfileSharedPreferences,
    private val userProfileImageLocalStorage: ProfileImageLocalStorage
) : UserProfileRepository {
    override suspend fun getUserProfile(): UserProfile? {
        return supabaseUserProfile.getUserProfile()
    }

    override suspend fun getUserProfileFromLocal(): UserProfile {
        return userProfileSharedPreferences.getUserProfile()
    }

    override suspend fun setUserProfileImageLocal() {
        val imageByteArray = getProfileImage()
        if(imageByteArray != null) {
            userProfileImageLocalStorage.saveProfileImageToInternalStorage(imageByteArray)
        } else {
            Log.d("ProfileImageLocalStorage", "null")
        }
    }

    override suspend fun getUserProfileImageFromLocal(): ByteArray? {
        return userProfileImageLocalStorage.loadProfileImageFromInternalStorage()
    }

    override suspend fun setUserProfileLocal() {
        val userProfile = getUserProfile()
        if(userProfile != null)
            userProfileSharedPreferences.saveUserProfile(userProfile)
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