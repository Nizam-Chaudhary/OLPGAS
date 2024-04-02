package com.example.olpgas.manage_room.data.repository

import androidx.lifecycle.LiveData
import com.example.olpgas.browse_rooms.data.local.database.BrowseRoomDatabase
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.manage_room.domain.repository.ManageRoomRepository

class ManageRoomRepositoryImpl(
    private val allRoomsDetailsDatabase: BrowseRoomDatabase
) : ManageRoomRepository{
    override fun getAllOwnedRooms(ownerId: String): LiveData<List<AllRoomsDetailsLocal>> {
        return allRoomsDetailsDatabase.getAllRoomDetailsDao().getAllOwnedRooms(ownerId)
    }
}