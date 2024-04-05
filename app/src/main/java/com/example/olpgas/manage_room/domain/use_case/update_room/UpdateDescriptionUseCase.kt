package com.example.olpgas.manage_room.domain.use_case.update_room

import com.example.olpgas.manage_room.domain.repository.ManageRoomRepository

class UpdateDescriptionUseCase(
    private val manageRoomRepository: ManageRoomRepository
) {
    suspend operator fun invoke(
        id: Int,
        roomFeatureId: Int,
        description: String
    ) {
        manageRoomRepository.updateDescription(roomFeatureId, description)
        manageRoomRepository.updateDescriptionAllRoomDetails(id, description)
        manageRoomRepository.updateDescriptionFullRoomDetails(id, description)
    }
}