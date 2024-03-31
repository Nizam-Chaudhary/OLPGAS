package com.example.olpgas.main_activity.domain.use_case

import com.example.olpgas.auth.domain.repository.AuthRepository

class UserLoggedInStatus(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() : Boolean {
        return repository.isUserLoggedIn()
    }
}