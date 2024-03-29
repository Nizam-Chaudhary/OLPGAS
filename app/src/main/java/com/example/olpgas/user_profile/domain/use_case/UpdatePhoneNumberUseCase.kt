package com.example.olpgas.user_profile.domain.use_case

import com.example.olpgas.user_profile.domain.repository.UserProfileRepository

class UpdatePhoneNumberUseCase(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(phoneNumber: String) {
        repository.updatePhoneNumber(phoneNumber)
    }
}