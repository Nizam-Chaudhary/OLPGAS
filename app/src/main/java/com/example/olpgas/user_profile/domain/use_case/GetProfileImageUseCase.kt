package com.example.olpgas.user_profile.domain.use_case

import com.example.olpgas.user_profile.domain.repository.UserProfileRepository

class GetProfileImageUseCase(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke() : ByteArray? {
        return repository.getUserProfileImageFromLocal()
    }
}