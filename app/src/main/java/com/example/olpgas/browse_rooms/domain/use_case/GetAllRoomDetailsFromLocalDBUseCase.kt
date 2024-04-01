package com.example.olpgas.browse_rooms.domain.use_case

import androidx.lifecycle.LiveData
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.browse_rooms.domain.repository.BrowseRoomsRepository

class GetAllRoomDetailsFromLocalDBUseCase(
    private val repository: BrowseRoomsRepository
) {
    operator fun invoke() : LiveData<List<AllRoomsDetailsLocal>>{
        return repository.getAllRoomsDetailsFromLocalDB()
    }
}