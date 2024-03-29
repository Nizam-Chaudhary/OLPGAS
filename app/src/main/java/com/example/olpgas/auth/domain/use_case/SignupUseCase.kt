package com.example.olpgas.auth.domain.use_case

import com.example.olpgas.auth.domain.model.SignupResult
import com.example.olpgas.auth.domain.repository.AuthRepository
import com.example.olpgas.auth.domain.util.ValidationUtil

class SignupUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        userName: String,
        email: String,
        password: String,
        confirmPassword: String
    ) : SignupResult{
        val userNameError = ValidationUtil.validateUserName(userName)
        val emailError = ValidationUtil.validateEmail(email)
        val passwordError = ValidationUtil.validatePassword(password)
        val confirmPasswordError = ValidationUtil.validateConfirmPassword(password, confirmPassword)

        if(userNameError != null || emailError != null || passwordError != null || confirmPasswordError != null) {
            return SignupResult(
                userNameError, emailError, passwordError, confirmPasswordError
            )
        }

        val result = repository.signUp(email, password)

        return SignupResult(
            result = result
        )
    }
}