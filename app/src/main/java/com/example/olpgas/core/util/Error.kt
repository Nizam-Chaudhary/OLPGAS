package com.example.olpgas.core.util

sealed class Error {
    data object FieldEmpty: Error()
    data object InValidEmail: Error()
    data object InputTooShort: Error()
    data object PasswordsNotMatching: Error()
    data object EmptyField: Error()
    data object InValidAge: Error()
    data object InvalidPhoneNumber: Error()
    data object NoItemSelected: Error()
}