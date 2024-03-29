package com.example.olpgas.auth.presentation.signup_activity

sealed class SignupEvent {
    data class EnteredUserName(val userName: String): SignupEvent()
    data class EnteredEmail(val email: String): SignupEvent()
    data class EnteredPassword(val password: String): SignupEvent()
    data class EnteredConfirmPassword(val confirmPassword: String): SignupEvent()
    data object Signup: SignupEvent()

}