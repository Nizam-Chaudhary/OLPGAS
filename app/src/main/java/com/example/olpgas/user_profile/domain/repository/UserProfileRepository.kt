package com.example.olpgas.user_profile.domain.repository

import com.example.olpgas.user_profile.data.model.UserProfile

interface UserProfileRepository {
    suspend fun getUserProfile() : UserProfile?

    suspend fun setUserProfileLocal()

    suspend fun getUserProfileFromLocal(): UserProfile

    suspend fun updateGender(gender: String)

    suspend fun updateAge(age: Int)

    suspend fun updatePhoneNumber(phoneNumber: String)

    suspend fun updateAddress(
        streetNumber: String,
        city: String,
        state: String
    )

    suspend fun getProfileImage() : ByteArray?

    suspend fun uploadProfileImage(
        imageByteArray: ByteArray
    )

    suspend fun setUserProfileImageLocal()

    suspend fun getUserProfileImageFromLocal() : ByteArray?

    suspend fun upsertUser(
        userProfile: UserProfile
    )

    suspend fun setUpUserWithGoogle()
}