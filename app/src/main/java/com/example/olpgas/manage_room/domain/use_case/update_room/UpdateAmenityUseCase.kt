package com.example.olpgas.manage_room.domain.use_case.update_room

import com.example.olpgas.manage_room.domain.repository.ManageRoomRepository

class UpdateAmenityUseCase(
    private val manageRoomRepository: ManageRoomRepository
) {
    suspend operator fun invoke(
        id: Int,
        roomFeatureId: Int,
        amenity: List<String>
    ) {
        manageRoomRepository.updateAmenity(roomFeatureId, amenity)
        manageRoomRepository.updateAmenityFullRoomDetails(id, amenity)
    }
}