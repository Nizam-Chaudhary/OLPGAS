package com.example.olpgas.profile.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userName: String,
    val email: String? = null,
    val age: Int,
    val gender: String,
    val phoneNumber: String,
    val streetNumber: String,
    val city: String,
    val state: String,
)
