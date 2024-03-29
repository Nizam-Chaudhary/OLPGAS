package com.example.olpgas.auth.presentation.login_activity

sealed class LoginState {
    data object Loading: LoginState()
    data class Error(val message: String?): LoginState()
    data object Success: LoginState()
}