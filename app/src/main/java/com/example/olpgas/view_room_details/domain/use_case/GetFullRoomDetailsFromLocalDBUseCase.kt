package com.example.olpgas.view_room_details.domain.use_case

import androidx.lifecycle.LiveData
import com.example.olpgas.view_room_details.data.local.database.entities.FullRoomDetailsLocal
import com.example.olpgas.view_room_details.domain.repository.ViewRoomDetailsRepository

class GetFullRoomDetailsFromLocalDBUseCase(
    private val repository: ViewRoomDetailsRepository
) {
    operator fun invoke(id: Int): LiveData<FullRoomDetailsLocal> {
        return repository.getFullRoomDetails(id)
    }
}