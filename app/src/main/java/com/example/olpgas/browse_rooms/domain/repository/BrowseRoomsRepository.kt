package com.example.olpgas.browse_rooms.domain.repository

import androidx.lifecycle.LiveData
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.browse_rooms.data.remote.model.AllRoomDetails
import com.example.olpgas.manage_room.data.remote.SupabaseManageRoom

interface BrowseRoomsRepository {
    suspend fun getRoomsForListing() : List<AllRoomDetails>?

    suspend fun getRoomsImageForListing(ownerId: String, id: Int) : String?

    suspend fun upsert(allRoomsDetails: AllRoomsDetailsLocal)

    fun getAllRoomDetailsFromLocalDB() : LiveData<List<AllRoomsDetailsLocal>>

    suspend fun delete(id: Int)

    suspend fun getAllRoomIds() : List<SupabaseManageRoom.Id>?

    suspend fun getAllRoomIdsFromLocal() : List<Int>
}
