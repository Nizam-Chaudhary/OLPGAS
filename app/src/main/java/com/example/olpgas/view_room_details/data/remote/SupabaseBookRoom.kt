package com.example.olpgas.view_room_details.data.remote

import android.util.Log
import com.example.olpgas.bookings_history.data.remote.model.RoomBooking
import com.example.olpgas.core.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

class SupabaseBookRoom {

    suspend fun bookRoom(
        booking: RoomBooking
    ) {
       try {
           SupabaseClient.client.postgrest.from("BookMaster")
               .insert(booking)
       }catch (e: Exception) {
           e.printStackTrace()
           Log.e(TAG, "Error Booking Room")
       }
    }

    companion object {
        const val TAG = "Supabase Book Room"
    }
}