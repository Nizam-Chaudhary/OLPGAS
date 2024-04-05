package com.example.olpgas.bookings_history.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.olpgas.bookings_history.data.local.database.entity.RoomBookingLocal

@Dao
interface RoomBookingDao {
    @Upsert
    suspend fun upsert(roomBookingLocal: RoomBookingLocal)

    @Query("SELECT * FROM RoomBookingLocal Where userId = :userId")
    fun getAllRoomBookingsUser(userId: String) : LiveData<List<RoomBookingLocal>>
}