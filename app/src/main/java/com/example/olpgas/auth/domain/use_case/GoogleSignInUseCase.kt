package com.example.olpgas.auth.domain.use_case

import com.example.olpgas.auth.domain.repository.AuthRepository
import com.example.olpgas.core.util.SimpleResource

class GoogleSignInUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        rawNonce: String,
        googleIdToken: String
    ) : SimpleResource {
        return repository.signInWithGoogle(googleIdToken, rawNonce)
    }


}