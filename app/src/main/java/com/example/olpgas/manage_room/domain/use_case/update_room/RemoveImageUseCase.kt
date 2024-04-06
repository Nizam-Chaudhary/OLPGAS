package com.example.olpgas.manage_room.domain.use_case.update_room

import com.example.olpgas.bookings_history.domain.repository.RoomBookingRepository
import com.example.olpgas.manage_room.data.repository.ManageRoomRepositoryImpl
import com.example.olpgas.manage_room.domain.repository.ManageRoomRepository

class RemoveImageUseCase(
    val repository: ManageRoomRepository
) {
    suspend operator fun invoke(
        ownerId: String,
        id: Int,
        fileName: String
    ) {
        repository.removeImage(ownerId, id, fileName)
    }
}