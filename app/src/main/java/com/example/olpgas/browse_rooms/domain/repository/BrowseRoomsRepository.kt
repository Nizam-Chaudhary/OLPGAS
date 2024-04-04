package com.example.olpgas.browse_rooms.domain.repository

import androidx.lifecycle.LiveData
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.browse_rooms.data.remote.model.AllRoomDetails

interface BrowseRoomsRepository {
    suspend fun getRoomsForListing() : List<AllRoomDetails>?

    suspend fun getRoomsImageForListing(ownerId: String, id: Int) : String?

    suspend fun upsert(allRoomsDetails: AllRoomsDetailsLocal)

    fun getAllRoomDetailsFromLocalDB() : LiveData<List<AllRoomsDetailsLocal>>

    suspend fun deleteAllFromLocal()
}
