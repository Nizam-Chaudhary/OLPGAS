package com.example.olpgas.user_profile.domain.use_case

import com.example.olpgas.user_profile.domain.repository.UserProfileRepository

class UpdateAgeUseCase(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(age: Int) {
        repository.updateAge(age)
    }
}