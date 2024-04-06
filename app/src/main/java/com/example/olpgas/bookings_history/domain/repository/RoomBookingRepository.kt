package com.example.olpgas.bookings_history.domain.repository

import androidx.lifecycle.LiveData
import com.example.olpgas.bookings_history.data.local.database.entity.RoomBookingLocal
import com.example.olpgas.bookings_history.data.remote.model.BookingView

interface RoomBookingRepository {
    suspend fun upsert(roomBooking: RoomBookingLocal)
    suspend fun getAllBookings() : List<BookingView>?
    suspend fun getAllRoomBookingsUserFromLocal(userId: String) : LiveData<List<RoomBookingLocal>>

    suspend fun getAllRoomBookingsUserFromLocalForOwner(userId: String) : LiveData<List<RoomBookingLocal>>

    suspend fun getRoomsImageForListing(ownerId: String, id: Int) : String?
}