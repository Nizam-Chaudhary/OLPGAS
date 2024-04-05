package com.example.olpgas.bookings_history.data.remote

import android.util.Log
import com.example.olpgas.bookings_history.data.remote.model.RoomBooking
import com.example.olpgas.core.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

class SupabaseBookings {

    suspend fun getAllBookings() : List<RoomBooking>? {
        return try {
            SupabaseClient.client.postgrest.from("BookMaster")
                .select().decodeList<RoomBooking>()
        }catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Error: ${e.message}")
            null
        }
    }

    companion object {
        const val TAG = "Supabase Bookings"
    }
}