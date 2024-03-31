package com.example.olpgas.user_profile.presentation

sealed class UserProfileEvent {
    data class UpdateProfileImage(val imageByteArray: ByteArray) : UserProfileEvent() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as UpdateProfileImage

            return imageByteArray.contentEquals(other.imageByteArray)
        }

        override fun hashCode(): Int {
            return imageByteArray.contentHashCode()
        }
    }

    data class UpdateGender(val gender: String) : UserProfileEvent()
    data class UpdateAge(val age: Int) : UserProfileEvent()
    data class UpdatePhoneNumber(val phoneNumber: String) : UserProfileEvent()
    data class UpdateAddress(
        val streetNumber: String,
        val city: String,
        val state: String,
    ) : UserProfileEvent()

    data object SetUserProfile: UserProfileEvent()
}