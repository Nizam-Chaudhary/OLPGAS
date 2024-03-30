package com.example.olpgas.auth.presentation.signup_activity

sealed class SignupState {
    data class Loading(val isLoading: Boolean): SignupState()
    data class Error(val message: String?): SignupState()
    data object Success: SignupState()
}