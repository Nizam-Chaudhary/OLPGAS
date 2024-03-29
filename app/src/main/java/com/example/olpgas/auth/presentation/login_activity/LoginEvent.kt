package com.example.olpgas.auth.presentation.login_activity

sealed class LoginEvent {
    data class EnteredEmail(val email: String): LoginEvent()
    data class EnteredPassword(val password: String): LoginEvent()
    data object Login: LoginEvent()
    data class GoogleSignIn(
        val rawNonce: String,
        val googleIdToken: String
    ): LoginEvent()
}