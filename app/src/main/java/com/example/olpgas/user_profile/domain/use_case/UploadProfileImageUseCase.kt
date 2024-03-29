package com.example.olpgas.user_profile.domain.use_case

import com.example.olpgas.user_profile.domain.repository.UserProfileRepository

class UploadProfileImageUseCase(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(imageByteArray: ByteArray) {
        repository.uploadProfileImage(imageByteArray)
    }
}