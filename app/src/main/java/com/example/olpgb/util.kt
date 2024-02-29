package com.example.olpgb

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[\\w!#$%&'*+/=?^`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^`{|}~-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$".toRegex()
    return emailRegex.matches(email)
}