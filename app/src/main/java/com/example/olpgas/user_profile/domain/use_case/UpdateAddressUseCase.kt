package com.example.olpgas.user_profile.domain.use_case

import com.example.olpgas.user_profile.domain.repository.UserProfileRepository

class UpdateAddressUseCase(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(
       streetNumber: String,
       city: String,
       state: String
    ) {
        repository.updateAddress(streetNumber, city, state)
    }
}