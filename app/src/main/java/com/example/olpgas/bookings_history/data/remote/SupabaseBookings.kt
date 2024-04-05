package com.example.olpgas.bookings_history.data.remote

import android.util.Log
import com.example.olpgas.bookings_history.data.remote.model.BookingView
import com.example.olpgas.bookings_history.presentation.BookingsRecyclerViewAdapter
import com.example.olpgas.browse_rooms.data.remote.SupabaseListRooms
import com.example.olpgas.view_room_details.data.remote.model.RoomBooking
import com.example.olpgas.core.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage

class SupabaseBookings {

    suspend fun getAllBookings() : List<BookingView>? {
        return try {
            SupabaseClient.client.postgrest.from("BookingsView")
                .select().decodeList<BookingView>()
        }catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Error: ${e.message}")
            null
        }
    }

    suspend fun getRoomsImageForListing(ownerId: String, id: Int) : String? {
        return try {
            val bucket = SupabaseClient.client.storage.from("RoomPics")
            val files = bucket.list("${ownerId}/${id}")
            bucket.publicUrl("${ownerId}/${id}/${files[0].name}")
        } catch (e: Exception) {
            Log.e(SupabaseListRooms.TAG, "Error: ${e.message}")
            null
        }
    }

    companion object {
        const val TAG = "Supabase Bookings"
    }
}