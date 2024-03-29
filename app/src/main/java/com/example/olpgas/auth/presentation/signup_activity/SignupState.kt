package com.example.olpgas.auth.presentation.signup_activity

sealed class SignupState {
    data object Loading: SignupState()
    data class Error(val message: String?): SignupState()
    data object Success: SignupState()
}