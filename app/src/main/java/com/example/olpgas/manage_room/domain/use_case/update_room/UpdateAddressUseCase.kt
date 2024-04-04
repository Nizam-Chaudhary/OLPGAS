package com.example.olpgas.manage_room.domain.use_case.update_room

import com.example.olpgas.manage_room.domain.repository.ManageRoomRepository

class UpdateAddressUseCase(
    private val manageRoomRepository: ManageRoomRepository
) {
    suspend operator fun invoke(
        id: Int,
        streetNumber: String,
        landMark: String,
        state: String,
        city: String
    ) {
        manageRoomRepository.updateAddress(id, streetNumber, landMark, state, city)
        manageRoomRepository.updateAddressFullRoomDetails(id, streetNumber, landMark, state, city)
        manageRoomRepository.updateAddressAllRoomDetails(id, state, city)
    }
}