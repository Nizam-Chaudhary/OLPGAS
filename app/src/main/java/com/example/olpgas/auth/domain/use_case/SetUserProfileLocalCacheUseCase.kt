package com.example.olpgas.auth.domain.use_case

import com.example.olpgas.user_profile.domain.repository.UserProfileRepository

class SetUserProfileLocalCacheUseCase(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke() {
        repository.setUserProfileLocal()
        repository.setUserProfileImageLocal()
    }
}