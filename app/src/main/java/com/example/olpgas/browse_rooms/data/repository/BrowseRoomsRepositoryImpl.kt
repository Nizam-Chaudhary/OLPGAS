package com.example.olpgas.browse_rooms.data.repository

import com.example.olpgas.browse_rooms.data.remote.SupabaseListRooms
import com.example.olpgas.browse_rooms.data.remote.model.AllRoomDetails
import com.example.olpgas.browse_rooms.domain.repository.BrowseRoomsRepository

class BrowseRoomsRepositoryImpl(
    private val supabaseListRooms: SupabaseListRooms
) : BrowseRoomsRepository {
    override suspend fun getRoomsForListing(): List<AllRoomDetails>? {
        return supabaseListRooms.getRoomsForListing()
    }

    override suspend fun getRoomImageForListing(ownerIds: List<String>, roomNames: List<String>): List<String>? {
        return supabaseListRooms.getRoomsImageForListing(ownerIds, roomNames)
    }
}