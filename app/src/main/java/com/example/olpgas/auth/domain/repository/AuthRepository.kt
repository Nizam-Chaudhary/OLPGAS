package com.example.olpgas.auth.domain.repository

import com.example.olpgas.core.util.SimpleResource

interface AuthRepository {

    suspend fun login(
        userEmail: String,
        userPassword: String,
    ) : SimpleResource

    suspend fun signUp(
        userEmail: String,
        userPassword: String,
    ) : SimpleResource

    suspend fun logout() : SimpleResource

    suspend fun signInWithGoogle(
        googleIdToken: String,
        rawNonce: String
    ) : SimpleResource

    suspend fun isUserLoggedIn() : Boolean
}