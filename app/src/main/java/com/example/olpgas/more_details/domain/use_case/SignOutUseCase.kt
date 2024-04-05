package com.example.olpgas.more_details.domain.use_case

import com.example.olpgas.auth.domain.repository.AuthRepository
import com.example.olpgas.user_profile.domain.repository.UserProfileRepository

class SignOutUseCase(
    private val authRepository: AuthRepository,
    private val userProfileRepository: UserProfileRepository
) {
    suspend operator fun invoke() {
        userProfileRepository.clearUserProfileFromLocal()
        authRepository.logout()
    }
}