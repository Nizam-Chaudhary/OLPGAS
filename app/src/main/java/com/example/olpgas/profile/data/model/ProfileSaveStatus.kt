package com.example.olpgas.profile.data.model

sealed class ProfileSaveStatus {
    data object Success: ProfileSaveStatus()
    data class Error(val message: String): ProfileSaveStatus()
}