package com.example.olpgas.view_room_details.data.repository

import androidx.lifecycle.LiveData
import com.example.olpgas.view_room_details.data.remote.model.RoomBooking
import com.example.olpgas.browse_rooms.data.remote.model.RoomBookingStatus
import com.example.olpgas.view_room_details.data.local.database.FullRoomDetailsDatabase
import com.example.olpgas.view_room_details.data.local.database.entities.FullRoomDetailsLocal
import com.example.olpgas.view_room_details.data.remote.SupabaseBookRoom
import com.example.olpgas.view_room_details.data.remote.SupabaseRoomDetails
import com.example.olpgas.view_room_details.data.remote.model.FullRoomDetails
import com.example.olpgas.view_room_details.domain.repository.ViewRoomDetailsRepository

class ViewRoomDetailsRepositoryImpl(
    private val supabaseRoomDetails: SupabaseRoomDetails,
    private val supabaseBookRoom: SupabaseBookRoom,
    private val database: FullRoomDetailsDatabase
) : ViewRoomDetailsRepository {
    override suspend fun upsert(fullRoomDetailsLocal: FullRoomDetailsLocal) {
        database.getFullRoomDetailsDao().upsert(fullRoomDetailsLocal)
    }

    override fun getFullRoomDetails(id: Int): LiveData<FullRoomDetailsLocal> {
        return database.getFullRoomDetailsDao().getFullRoomDetails(id)
    }

    override suspend fun getAllFullRoomDetails(): List<FullRoomDetails>? {
        return supabaseRoomDetails.getAllFullRoomDetails()
    }

    override suspend fun getAllFullRoomDetailsImages(
        ownerIds: String,
        id: Int
    ): List<String>? {
        return supabaseRoomDetails.getFullRoomDetailsImages(ownerIds, id)
    }

    override suspend fun delete(id: Int) {
        database.getFullRoomDetailsDao().delete(id)
    }

    override suspend fun bookRoom(
        booking: RoomBooking,
        roomBookingStatus: RoomBookingStatus,
        totalOccupiedBy: Int
    ) {
        supabaseBookRoom.bookRoom(booking, roomBookingStatus, totalOccupiedBy)
    }
}