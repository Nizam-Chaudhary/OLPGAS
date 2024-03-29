package com.example.olpgas.auth.domain.util

import com.example.olpgas.auth.presentation.util.AuthError

class ValidationUtil {
    companion object {
        fun validateEmail(email: String) : AuthError? {
            val trimmedEmail = email.trim()
            val emailRegex = "^[\\w!#$%&'*+/=?^`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^`{|}~-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$".toRegex()
            if(trimmedEmail.isEmpty()) {
                return AuthError.FieldEmpty
            }
            if(!emailRegex.matches(trimmedEmail)) {
                return AuthError.InValidEmail
            }
            return null
        }

        fun validatePassword(password: String) : AuthError? {
            val trimmedPassword = password.trim()
            if(trimmedPassword.isEmpty()) {
                return AuthError.FieldEmpty
            }

            if(trimmedPassword.length < 8) {
                return AuthError.InputTooShort
            }
            return null
        }

        fun validateUserName(userName: String) : AuthError? {
            val trimmedUserName = userName.trim()
            if(trimmedUserName.isEmpty()) {
                return AuthError.FieldEmpty
            }
            return null
        }

        fun validateConfirmPassword(password: String, confirmPassword: String) : AuthError? {
            val trimmedPassword = password.trim()
            val trimmedConfirmPassword = confirmPassword.trim()
            if(trimmedPassword != trimmedConfirmPassword) {
                return AuthError.PasswordsNotMatching
            }
            return null
        }
    }
}