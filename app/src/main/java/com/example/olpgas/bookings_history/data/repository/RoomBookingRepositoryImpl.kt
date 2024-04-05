package com.example.olpgas.bookings_history.data.repository

import androidx.lifecycle.LiveData
import com.example.olpgas.bookings_history.data.local.database.RoomBookingDatabase
import com.example.olpgas.bookings_history.data.local.database.entity.RoomBookingLocal
import com.example.olpgas.bookings_history.data.remote.SupabaseBookings
import com.example.olpgas.bookings_history.data.remote.model.RoomBooking
import com.example.olpgas.bookings_history.domain.repository.RoomBookingRepository

class RoomBookingRepositoryImpl(
    private val database: RoomBookingDatabase,
    private val supabaseBookings: SupabaseBookings
) : RoomBookingRepository{
    override suspend fun upsert(roomBooking: RoomBookingLocal) {
        database.getRoomBookingDao().upsert(roomBooking)
    }

    override suspend fun getAllBookings(): List<RoomBooking>? {
        return supabaseBookings.getAllBookings()
    }

    override suspend fun getAllRoomBookingsUserFromLocal(userId: String) : LiveData<List<RoomBookingLocal>> {
        return database.getRoomBookingDao().getAllRoomBookingsUser(userId)
    }
}