package com.example.olpgas.user_profile.domain.use_case

import com.example.olpgas.user_profile.data.model.UserProfile
import com.example.olpgas.user_profile.domain.repository.UserProfileRepository

class GetUserProfileUseCase(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke() : UserProfile {
        return repository.getUserProfileFromLocal()
    }
}