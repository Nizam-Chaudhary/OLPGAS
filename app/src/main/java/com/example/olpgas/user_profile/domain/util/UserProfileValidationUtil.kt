package com.example.olpgas.user_profile.domain.util

import com.example.olpgas.core.util.Error

class UserProfileValidationUtil {
    companion object {
        fun validateAge(age: Int) : Error? {
            if(age == 0) {
                return Error.EmptyField
            }
            if(age < 12 || age > 169) {
                return Error.InValidAge
            }
            return null
        }

        fun validatePhoneNumber(phoneNumber: String) : Error? {
            val trimmedPhoneNumber = phoneNumber.trim()
            if(trimmedPhoneNumber.isEmpty()) {
                return Error.EmptyField
            }
            if(trimmedPhoneNumber.length != 10) {
                return Error.InvalidPhoneNumber
            }
            return null
        }

        fun validateForEmpty(value: String) : Error? {
            if(value.trim().isEmpty()) {
                return Error.EmptyField
            }
            return null
        }
    }
}