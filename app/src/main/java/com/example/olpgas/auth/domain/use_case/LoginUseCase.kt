package com.example.olpgas.auth.domain.use_case

import com.example.olpgas.auth.domain.model.LoginResult
import com.example.olpgas.auth.domain.repository.AuthRepository
import com.example.olpgas.auth.domain.util.ValidationUtil

class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        userEmail: String,
        userPassword: String
    ) : LoginResult {
        val emailError = ValidationUtil.validateEmail(userEmail)
        val passwordError = ValidationUtil.validatePassword(userPassword)

        if(emailError != null || passwordError != null) {
            return LoginResult(
                emailError = emailError,
                passwordError = passwordError
            )
        }

        val result = repository.login(userEmail, userPassword)

        return LoginResult(
            result = result
        )
    }
}