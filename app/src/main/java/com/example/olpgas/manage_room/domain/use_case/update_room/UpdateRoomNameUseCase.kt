package com.example.olpgas.manage_room.domain.use_case.update_room

import com.example.olpgas.manage_room.domain.repository.ManageRoomRepository

class UpdateRoomNameUseCase(
    private val manageRoomRepository: ManageRoomRepository
) {
    suspend operator fun invoke(
        id: Int,
        newRoomName: String
    ) {
        manageRoomRepository.updateRoomName(id, newRoomName)
        manageRoomRepository.updateRoomNameAllRoomDetails(id, newRoomName)
        manageRoomRepository.updateRoomNameFullRoomDetails(id, newRoomName)
    }
}