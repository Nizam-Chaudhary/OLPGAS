package com.example.olpgas.manage_room.domain.use_case.update_room

import com.example.olpgas.manage_room.domain.repository.ManageRoomRepository

class UpdateRoomAreaUseCase(
    private val manageRoomRepository: ManageRoomRepository
) {
    suspend operator fun invoke(
        id: Int,
        roomArea: Int
    ) {
        manageRoomRepository.updateRoomArea(id, roomArea)
        manageRoomRepository.updateRoomAreaFullRoomDetails(id, roomArea)
    }
}