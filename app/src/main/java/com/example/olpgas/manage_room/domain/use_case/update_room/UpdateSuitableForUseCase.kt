package com.example.olpgas.manage_room.domain.use_case.update_room

import com.example.olpgas.manage_room.domain.repository.ManageRoomRepository

class UpdateSuitableForUseCase(private val manageRoomRepository: ManageRoomRepository) {
    suspend operator fun invoke(
        id: Int,
        roomFeatureId: Int,
        amenity: List<String>
    ) {
        manageRoomRepository.updateSuitableFor(roomFeatureId, amenity)
        manageRoomRepository.updateSuitableForFullRoomDetails(id, amenity)
    }
}