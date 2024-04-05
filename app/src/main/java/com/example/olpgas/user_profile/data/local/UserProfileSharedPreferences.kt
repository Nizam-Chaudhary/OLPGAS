package com.example.olpgas.user_profile.data.local

import android.content.SharedPreferences
import com.example.olpgas.user_profile.data.model.UserProfile

class UserProfileSharedPreferences(
    private val userProfileSharedPreferences: SharedPreferences
) {

    fun saveUserProfile(userProfile: UserProfile) {
        userProfileSharedPreferences.edit()
            .putString(USER_ID_KEY, userProfile.userId)
            .putString(USER_NAME_KEY, userProfile.userName)
            .putString(EMAIL_KEY, userProfile.email)
            .putInt(AGE_KEY, userProfile.age ?: 0)
            .putString(GENDER_KEY, userProfile.gender)
            .putString(PHONE_NUMBER_KEY, userProfile.phoneNumber)
            .putString(STREET_NUMBER_KEY, userProfile.streetNumber)
            .putString(CITY_KEY, userProfile.city)
            .putString(STATE_KEY, userProfile.state)
            .apply()
    }

    fun getUserProfile() : UserProfile {
        return UserProfile(
            userId = userProfileSharedPreferences.getString(USER_ID_KEY, null),
            userName = userProfileSharedPreferences.getString(USER_NAME_KEY, null),
            email = userProfileSharedPreferences.getString(EMAIL_KEY, null),
            age = userProfileSharedPreferences.getInt(AGE_KEY, 0),
            gender = userProfileSharedPreferences.getString(GENDER_KEY, null),
            phoneNumber = userProfileSharedPreferences.getString(PHONE_NUMBER_KEY, null),
            streetNumber = userProfileSharedPreferences.getString(STREET_NUMBER_KEY, null),
            city = userProfileSharedPreferences.getString(CITY_KEY, null),
            state = userProfileSharedPreferences.getString(STATE_KEY, null)
        )
    }

    fun getUserName() : String? {
        return userProfileSharedPreferences.getString(USER_NAME_KEY, null)
    }

    fun updateGender(gender: String) {
        userProfileSharedPreferences.edit()
            .putString(GENDER_KEY, gender)
            .apply()
    }

    fun updateAge(age: Int) {
        userProfileSharedPreferences.edit()
            .putInt(AGE_KEY, age)
            .apply()
    }

    fun updatePhoneNumber(phoneNumber: String) {
        userProfileSharedPreferences.edit()
            .putString(PHONE_NUMBER_KEY, phoneNumber)
            .apply()
    }

    fun updateAddress(
        streetNumber: String,
        city: String,
        state: String
    ) {
        userProfileSharedPreferences.edit()
            .putString(STREET_NUMBER_KEY, streetNumber)
            .putString(CITY_KEY, city)
            .putString(STATE_KEY, state)
            .apply()
    }

    fun clearUserProfile() {
        userProfileSharedPreferences.edit().clear().apply()
    }

    companion object {
        const val USER_ID_KEY = "userId"
        const val USER_NAME_KEY = "userName"
        const val EMAIL_KEY = "email"
        const val AGE_KEY = "age"
        const val GENDER_KEY = "gender"
        const val PHONE_NUMBER_KEY = "phoneNumber"
        const val STREET_NUMBER_KEY = "streetNumber"
        const val CITY_KEY = "city"
        const val STATE_KEY = "state"
    }
}