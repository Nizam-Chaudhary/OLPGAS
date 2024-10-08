package com.example.olpgas.bookings_history.data.repository

import androidx.lifecycle.LiveData
import com.example.olpgas.bookings_history.data.local.database.RoomBookingDatabase
import com.example.olpgas.bookings_history.data.local.database.entity.RoomBookingLocal
import com.example.olpgas.bookings_history.data.remote.SupabaseBookings
import com.example.olpgas.bookings_history.data.remote.model.BookingView
import com.example.olpgas.bookings_history.domain.repository.RoomBookingRepository

class RoomBookingRepositoryImpl(
    private val database: RoomBookingDatabase,
    private val supabaseBookings: SupabaseBookings
) : RoomBookingRepository{
    override suspend fun upsert(roomBooking: RoomBookingLocal) {
        database.getRoomBookingDao().upsert(roomBooking)
    }

    override suspend fun getAllBookings(): List<BookingView>? {
        return supabaseBookings.getAllBookings()
    }

    override suspend fun getAllRoomBookingsUserFromLocal(userId: String) : LiveData<List<RoomBookingLocal>> {
        return database.getRoomBookingDao().getAllRoomBookingsUser(userId)
    }

    override suspend fun getRoomsImageForListing(ownerId: String, id: Int): String? {
        return supabaseBookings.getRoomsImageForListing(ownerId, id)
    }

    override suspend fun getAllRoomBookingsUserFromLocalForOwner(userId: String): LiveData<List<RoomBookingLocal>> {
        return database.getRoomBookingDao().getAllRoomBookingsUserForOwner(userId)
    }
}