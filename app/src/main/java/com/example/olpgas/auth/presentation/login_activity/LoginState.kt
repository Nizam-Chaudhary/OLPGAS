package com.example.olpgas.auth.presentation.login_activity

sealed class LoginState {
    data class Loading(val isLoading: Boolean): LoginState()
    data class Error(val message: String?): LoginState()
    data object Success: LoginState()
}