package com.example.olpgas.manage_room.domain.use_case.update_room

import com.example.olpgas.manage_room.domain.repository.ManageRoomRepository
import com.example.olpgas.manage_room.presentation.post_room.PostRoomEvent

class UpdateShareableByUseCase (
    private val manageRoomRepository: ManageRoomRepository
) {
    suspend operator fun invoke(
        id: Int,
        shareableBy: Int
    ) {
        manageRoomRepository.updateShareableBy(id, shareableBy)
        manageRoomRepository.updateShareableByFullRoomDetails(id, shareableBy)
    }
}