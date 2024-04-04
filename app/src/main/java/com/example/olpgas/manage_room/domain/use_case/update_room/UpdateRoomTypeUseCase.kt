package com.example.olpgas.manage_room.domain.use_case.update_room

import com.example.olpgas.manage_room.domain.repository.ManageRoomRepository

class UpdateRoomTypeUseCase(
    private val manageRoomRepository: ManageRoomRepository
) {
    suspend operator fun invoke(
        id: Int,
        roomType: String
    ) {
        manageRoomRepository.updateRoomType(id, roomType)
        manageRoomRepository.updateRoomTypeFullRoomDetails(id, roomType)
    }
}