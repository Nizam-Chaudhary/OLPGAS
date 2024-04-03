package com.example.olpgas.manage_room.data.repository

import androidx.lifecycle.LiveData
import com.example.olpgas.browse_rooms.data.local.database.BrowseRoomDatabase
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.core.util.SimpleResource
import com.example.olpgas.manage_room.data.remote.SupabaseManageRoom
import com.example.olpgas.manage_room.data.remote.model.RoomDetails
import com.example.olpgas.manage_room.data.remote.model.RoomMaster
import com.example.olpgas.manage_room.domain.repository.ManageRoomRepository

class ManageRoomRepositoryImpl(
    private val allRoomsDetailsDatabase: BrowseRoomDatabase,
    private val supabaseManageRoom: SupabaseManageRoom
) : ManageRoomRepository{
    override fun getAllOwnedRooms(ownerId: String): LiveData<List<AllRoomsDetailsLocal>> {
        return allRoomsDetailsDatabase.getAllRoomDetailsDao().getAllOwnedRooms(ownerId)
    }

    override suspend fun upsertRoomDetails(roomDetails: RoomDetails): Int? {
        return supabaseManageRoom.upsertRoomDetails(roomDetails)
    }

    override suspend fun upsertRoomMaster(roomMaster: RoomMaster): SimpleResource {
        return supabaseManageRoom.upsertRoomMaster(roomMaster)
    }
}