package com.example.olpgas.user_profile.domain.use_case

import com.example.olpgas.user_profile.domain.repository.UserProfileRepository

class UpdateGenderUseCase(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(gender: String) {
        repository.updateGender(gender)
    }
}