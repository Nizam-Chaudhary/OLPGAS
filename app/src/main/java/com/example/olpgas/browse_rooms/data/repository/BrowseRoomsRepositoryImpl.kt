package com.example.olpgas.browse_rooms.data.repository

import androidx.lifecycle.LiveData
import com.example.olpgas.browse_rooms.data.local.database.BrowseRoomDatabase
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.browse_rooms.data.remote.SupabaseListRooms
import com.example.olpgas.browse_rooms.data.remote.model.AllRoomDetails
import com.example.olpgas.browse_rooms.domain.repository.BrowseRoomsRepository
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal as AllRoomDetailsLocal

class BrowseRoomsRepositoryImpl(
    private val supabaseListRooms: SupabaseListRooms,
    private val database: BrowseRoomDatabase
) : BrowseRoomsRepository {
    override suspend fun getRoomsForListing(): List<AllRoomDetails>? {
        return supabaseListRooms.getRoomsForListing()
    }

    override suspend fun getRoomImageForListing(ownerIds: List<String>, roomNames: List<String>): List<String>? {
        return supabaseListRooms.getRoomsImageForListing(ownerIds, roomNames)
    }

    override fun getAllRoomsDetailsFromLocalDB() : LiveData<List<AllRoomDetailsLocal>> {
        return database.getAllRoomDetailsDao().getAllRoomDetails()
    }

    override suspend fun saveAllRoomsToLocalDB(
        allRoomsDetails: List<AllRoomDetails>,
        allRoomsImages: List<String>
    ) {
        for(i in allRoomsDetails.indices) {
            val currentRoomDetails = allRoomsDetails[i]
            val currentRoomImage = allRoomsImages[i]
            val allRoomsRoomDetails = AllRoomsDetailsLocal(
                currentRoomDetails.id,
                currentRoomDetails.ownerId,
                currentRoomDetails.roomName,
                currentRoomDetails.roomNumber,
                currentRoomDetails.description,
                currentRoomDetails.roomFeatureId,
                currentRoomDetails.rentAmount,
                currentRoomDetails.deposit,
                currentRoomDetails.city,
                currentRoomDetails.state,
                currentRoomDetails.ratings,
                currentRoomDetails.bookingStatus,
                currentRoomImage,
            )
            database.getAllRoomDetailsDao().upsert(allRoomsRoomDetails)
        }
    }


}