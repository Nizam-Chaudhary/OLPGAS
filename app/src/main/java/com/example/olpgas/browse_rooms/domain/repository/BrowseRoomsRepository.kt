package com.example.olpgas.browse_rooms.domain.repository

import com.example.olpgas.browse_rooms.data.remote.model.AllRoomDetails

interface BrowseRoomsRepository {
    suspend fun getRoomsForListing() : List<AllRoomDetails>?

    suspend fun getRoomImageForListing(ownerIds: List<String>, roomNames: List<String>) : List<String>?
}