package com.example.olpgas.user_profile.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val userId: String? = null,
    val createdAt: String? = null,
    val userName: String? = null,
    val email: String? = null,
    val age: Int? = null,
    val gender: String? = null,
    val phoneNumber: String? = null,
    val streetNumber: String? = null,
    val city: String? = null,
    val state: String? = null
)