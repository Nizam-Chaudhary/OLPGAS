package com.example.olpgas.more_details.domain.use_case

import com.example.olpgas.user_profile.domain.repository.UserProfileRepository

class GetUserProfileImageUseCase(
    private val userProfileRepository: UserProfileRepository
) {
    suspend operator fun invoke() : ByteArray? {
        return userProfileRepository.getUserProfileImageFromLocal()
    }
}