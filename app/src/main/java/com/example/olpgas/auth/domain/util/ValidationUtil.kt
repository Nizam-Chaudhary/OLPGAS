package com.example.olpgas.auth.domain.util

import android.util.Log
import com.example.olpgas.core.util.Error

class ValidationUtil {
    companion object {
        fun validateEmail(email: String) : Error? {
            val trimmedEmail = email.trim()
            val emailRegex = "^[\\w!#$%&'*+/=?^`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^`{|}~-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$".toRegex()
            if(trimmedEmail.isEmpty()) {
                return Error.FieldEmpty
            }
            if(!emailRegex.matches(trimmedEmail)) {
                return Error.InValidEmail
            }
            return null
        }

        fun validatePassword(password: String) : Error? {
            val trimmedPassword = password.trim()
            if(trimmedPassword.isEmpty()) {
                return Error.FieldEmpty
            }

            if(trimmedPassword.length < 8) {
                return Error.InputTooShort
            }
            return null
        }

        fun validateUserName(userName: String) : Error? {
            val trimmedUserName = userName.trim()
            if(trimmedUserName.isEmpty()) {
                return Error.FieldEmpty
            }
            return null
        }

        fun validateConfirmPassword(password: String, confirmPassword: String) : Error? {
            val trimmedPassword = password.trim()
            val trimmedConfirmPassword = confirmPassword.trim()
            Log.d("Validation","password = $trimmedPassword, confirm Password = $trimmedConfirmPassword")
            if(trimmedPassword != trimmedConfirmPassword) {
                return Error.PasswordsNotMatching
            }
            return null
        }
    }
}