package com.example.olpgas.manage_room.domain.use_case.update_room

import com.example.olpgas.manage_room.domain.repository.ManageRoomRepository

class AddImageUseCase(
    private val manageRoomRepository: ManageRoomRepository
) {
    suspend operator fun invoke(
        ownerId: String,
        id: Int,
        imageByteArray: ByteArray
    ) {
        manageRoomRepository.addImage(ownerId, id, imageByteArray)
    }
}