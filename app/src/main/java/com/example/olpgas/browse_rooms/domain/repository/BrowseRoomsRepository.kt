package com.example.olpgas.browse_rooms.domain.repository

import androidx.lifecycle.LiveData
import com.example.olpgas.browse_rooms.data.remote.model.AllRoomDetails
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal as AllRoomDetailsLocal

interface BrowseRoomsRepository {
    suspend fun getRoomsForListing() : List<AllRoomDetails>?

    suspend fun getRoomImageForListing(ownerIds: List<String>, roomNames: List<String>) : List<String>?

    fun getAllRoomsDetailsFromLocalDB() : LiveData<List<AllRoomDetailsLocal>>

    suspend fun saveAllRoomsToLocalDB(allRoomsDetails: List<AllRoomDetails>, allRoomsImages: List<String>)
}