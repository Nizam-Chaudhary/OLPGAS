package com.example.olpgas.auth.data.repository

import com.example.olpgas.auth.data.remote.SupabaseAuth
import com.example.olpgas.auth.domain.repository.AuthRepository
import com.example.olpgas.core.util.SimpleResource

class AuthRepositoryImpl(
    private val supabaseAuth: SupabaseAuth
) : AuthRepository {
    override suspend fun login(userEmail: String, userPassword: String) : SimpleResource {
        return supabaseAuth.login(userEmail, userPassword)
    }

    override suspend fun signUp(
        userEmail: String,
        userPassword: String
    ) : SimpleResource {
        return supabaseAuth.signUp(userEmail, userPassword)
    }

    override suspend fun logout() : SimpleResource {
        return supabaseAuth.logout()
    }

    override suspend fun signInWithGoogle(googleIdToken: String, rawNonce: String) : SimpleResource {
        return supabaseAuth.signInWithGoogle(googleIdToken, rawNonce)
    }

    override suspend fun isUserLoggedIn() : Boolean {
        return supabaseAuth.isUserLoggedIn()
    }
}