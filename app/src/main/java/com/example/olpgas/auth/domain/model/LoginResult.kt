package com.example.olpgas.auth.domain.model

import com.example.olpgas.core.util.Error
import com.example.olpgas.core.util.SimpleResource

data class LoginResult(
    val emailError: Error? = null,
    val passwordError: Error? = null,
    val result: SimpleResource? = null
)