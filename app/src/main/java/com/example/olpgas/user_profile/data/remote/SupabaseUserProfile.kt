package com.example.olpgas.user_profile.data.remote

import android.content.SharedPreferences
import android.util.Log
import com.example.olpgas.core.data.remote.SupabaseClient
import com.example.olpgas.core.util.Constants.USER_ID
import com.example.olpgas.user_profile.data.model.UserProfile
import com.example.olpgas.user_profile.domain.util.Constants
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage

class SupabaseUserProfile(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val TAG = "Supabase Profile"
    }

    suspend fun getUserProfile() : UserProfile? {
        return try {
            SupabaseClient.client.postgrest.from(Constants.USER_DETAILS_TABLE)
                .select().decodeSingle<UserProfile>()
        }catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
            null
        }
    }

    suspend fun updateGender(gender: String) {
        try {
            SupabaseClient.client.postgrest.from(Constants.USER_DETAILS_TABLE)
                .update({
                    set(Constants.COL_GENDER, gender)
                })
        }catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
        }
    }

    suspend fun updateAge(age: Int) {
        try {
            SupabaseClient.client.postgrest.from(Constants.USER_DETAILS_TABLE)
                .update({
                    set(Constants.COL_AGE, age)
                })
        }catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
        }
    }

    suspend fun updatePhoneNumber(phoneNumber: String) {
        try {
            SupabaseClient.client.postgrest.from(Constants.USER_DETAILS_TABLE)
                .update({
                    set(Constants.COL_PHONE_NUMBER, phoneNumber)
                })
        }catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
        }
    }

    suspend fun updateAddress(
        streetNumber: String,
        city: String,
        state: String
    ) {
        try {
            SupabaseClient.client.postgrest.from(Constants.USER_DETAILS_TABLE)
                .update({
                    set(Constants.COL_STREET_NUMBER, streetNumber)
                    set(Constants.COL_CITY, city)
                    set(Constants.COL_STATE, state)
                })
        }catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
        }
    }

    suspend fun getProfileImage() : ByteArray? {
        return try {
            val bucket = SupabaseClient.client.storage.from(Constants.PROFILE_PIC_BUCKET)
            val ownerId = sharedPreferences.getString(USER_ID, null)
            ownerId?.let {
                return bucket.downloadAuthenticated("$it/${Constants.PROFILE_FILE_NAME}")
            }
            null
        }catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
            null
        }
    }

    suspend fun uploadProfileImage(
        imageByteArray: ByteArray
    ) {
        try {
            val bucket = SupabaseClient.client.storage.from(Constants.PROFILE_PIC_BUCKET)
            val ownerId = sharedPreferences.getString(USER_ID, null)
            ownerId?.let {
                bucket.upload("$ownerId/${Constants.PROFILE_FILE_NAME}", imageByteArray,upsert = true)
            }
        }catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
        }
    }

    suspend fun upsertUser(
        userProfile: UserProfile
    ) {
        try {
            SupabaseClient.client.postgrest.from(Constants.USER_DETAILS_TABLE)
                .upsert(userProfile)
        }catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
        }
    }
}