package com.example.olpgas.manage_room.domain.use_case.update_room

import com.example.olpgas.manage_room.domain.repository.ManageRoomRepository

class UpdateRentUseCase(
    private val manageRoomRepository: ManageRoomRepository
) {
    suspend operator fun invoke(
        id: Int,
        rent: Int
    ) {
        manageRoomRepository.updateRent(id, rent)
        manageRoomRepository.updateRentAllRoomDetails(id, rent)
        manageRoomRepository.updateRentFullRoomDetails(id, rent)
    }
}