package com.example.olpgas.core.util.domain.states

import com.example.olpgas.core.util.Error

data class TextFieldState(
    val text: String = "",
    val error: Error? = null
)