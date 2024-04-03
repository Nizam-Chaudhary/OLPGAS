package com.example.olpgas.manage_room.domain.repository

import androidx.lifecycle.LiveData
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.core.util.SimpleResource
import com.example.olpgas.manage_room.data.remote.model.RoomDetails
import com.example.olpgas.manage_room.data.remote.model.RoomMaster

interface ManageRoomRepository {
    fun getAllOwnedRooms(ownerId: String) : LiveData<List<AllRoomsDetailsLocal>>

    suspend fun upsertRoomDetails(roomDetails: RoomDetails) : Int?

    suspend fun upsertRoomMaster(roomMaster: RoomMaster) : SimpleResource
}