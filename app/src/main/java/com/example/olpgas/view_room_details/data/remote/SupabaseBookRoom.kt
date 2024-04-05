package com.example.olpgas.view_room_details.data.remote

import android.util.Log
import com.example.olpgas.bookings_history.data.remote.model.RoomBooking
import com.example.olpgas.browse_rooms.data.remote.model.RoomBookingStatus
import com.example.olpgas.core.data.remote.SupabaseClient
import com.example.olpgas.manage_room.presentation.update_room.UpdateRoomEvent
import io.github.jan.supabase.postgrest.postgrest

class SupabaseBookRoom {

    suspend fun bookRoom(
        booking: RoomBooking,
        roomBookingStatus: RoomBookingStatus,
        totalOccupiedBy: Int
    ) {
       try {
           SupabaseClient.client.postgrest.from("BookMaster")
               .insert(booking)

           SupabaseClient.client.postgrest.from("RoomMaster")
               .update({
                   set("bookingStatus", roomBookingStatus.value)
               }) {
                   filter {
                       eq("id", booking.roomId)
                   }
               }

           SupabaseClient.client.postgrest.from("RoomMaster")
               .update({
                   set("occupiedBy", totalOccupiedBy)
               }) {
                   filter {
                       eq("id", booking.roomId)
                   }
               }
       }catch (e: Exception) {
           e.printStackTrace()
           Log.e(TAG, "Error Booking Room")
       }
    }

    companion object {
        const val TAG = "Supabase Book Room"
    }
}