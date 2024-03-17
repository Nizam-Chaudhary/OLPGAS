package com.example.olpgas.profile.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userName: String? = null,
    val email: String? = null,
    val age: Int? = null,
    val gender: String? = null,
    val phoneNumber: String? = null,
    val streetNumber: String? = null,
    val city: String? = null,
    val state: String? = null,
)
