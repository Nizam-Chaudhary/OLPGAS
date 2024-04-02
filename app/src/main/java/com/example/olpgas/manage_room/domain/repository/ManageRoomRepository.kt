package com.example.olpgas.manage_room.domain.repository

import androidx.lifecycle.LiveData
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal

interface ManageRoomRepository {
    fun getAllOwnedRooms(ownerId: String) : LiveData<List<AllRoomsDetailsLocal>>
}