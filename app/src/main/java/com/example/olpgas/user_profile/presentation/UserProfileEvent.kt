package com.example.olpgas.user_profile.presentation

sealed class UserProfileEvent {
    data class updateProfileImage(val imageByteArray: ByteArray) : UserProfileEvent()
    data class updateGender(val gender: String) : UserProfileEvent()
    data class updateAge(val age: Int) : UserProfileEvent()
    data class updatePhoneNumber(val phoneNumber: String) : UserProfileEvent()
    data class updateAddress(
        val streetNumber: String,
        val city: String,
        val state: String,
    ) : UserProfileEvent()

    data object setUserProfile: UserProfileEvent()
}