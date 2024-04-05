package com.example.olpgas.more_details.domain.use_case

import com.example.olpgas.user_profile.domain.repository.UserProfileRepository

class GetUserNameUseCase(
    private val userProfileRepository: UserProfileRepository
) {
    suspend operator fun invoke() : String? {
        return userProfileRepository.getUserName()
    }
}