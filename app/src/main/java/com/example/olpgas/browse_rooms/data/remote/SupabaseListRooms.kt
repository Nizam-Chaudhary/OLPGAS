package com.example.olpgas.browse_rooms.data.remote

import android.util.Log
import com.example.olpgas.browse_rooms.data.remote.model.AllRoomDetails
import com.example.olpgas.browse_rooms.data.remote.model.RoomBookingStatus
import com.example.olpgas.browse_rooms.domain.util.Constants
import com.example.olpgas.core.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

class SupabaseListRooms {
    companion object {
        const val TAG = "Supabase List Rooms"
    }

    suspend fun getRoomsForListing() : List<AllRoomDetails>? {
        return try {
            SupabaseClient.client.postgrest.from(Constants.LIST_ROOM_DETAIL_TABLE)
                .select() {
                    filter {
                        neq(Constants.COL_BOOKING_STATUS, RoomBookingStatus.CompletelyBooked.value)
                    }
                }.decodeList<AllRoomDetails>()
        }catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
            null
        }
    }

    suspend fun getRoomsImageForListing(ownerId: String, roomName: String) : String? {
        return try {
            val bucket = SupabaseClient.client.storage.from("RoomPics")
            val files = bucket.list("${ownerId}/${roomName}")
            bucket.createSignedUrl("${ownerId}/${roomName}/${files[0].name}", Duration.INFINITE)
            bucket.publicUrl("${ownerId}/${roomName}/${files[0].name}")
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
            null
        }
    }
}