package com.example.olpgas.core.util.domain.states

import com.example.olpgas.auth.presentation.util.AuthError

data class TextFieldState(
    val text: String = "",
    val error: AuthError? = null
)