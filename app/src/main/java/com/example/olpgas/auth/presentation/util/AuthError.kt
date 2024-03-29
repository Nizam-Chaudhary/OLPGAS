package com.example.olpgas.auth.presentation.util

sealed class AuthError {
    data object FieldEmpty: AuthError()
    data object InValidEmail: AuthError()
    data object InputTooShort: AuthError()
    data object PasswordsNotMatching: AuthError()
    data object InValidPassword: AuthError()
}