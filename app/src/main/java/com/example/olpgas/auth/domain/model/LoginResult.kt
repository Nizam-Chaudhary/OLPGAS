package com.example.olpgas.auth.domain.model

import com.example.olpgas.auth.presentation.util.AuthError
import com.example.olpgas.core.util.SimpleResource

data class LoginResult(
    val emailError: AuthError? = null,
    val passwordError: AuthError? = null,
    val result: SimpleResource? = null
)