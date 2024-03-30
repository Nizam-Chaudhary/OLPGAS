package com.example.olpgas.auth.domain.model

import com.example.olpgas.core.util.Error
import com.example.olpgas.core.util.SimpleResource

data class SignupResult (
    val userNameError: Error? = null,
    val emailError: Error? = null,
    val passwordError: Error? = null,
    val confirmPasswordError: Error? = null,
    val result: SimpleResource? = null
)