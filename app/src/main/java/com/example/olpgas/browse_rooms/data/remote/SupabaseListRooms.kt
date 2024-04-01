package com.example.olpgas.browse_rooms.data.remote

import android.util.Log
import android.util.TimeUtils
import com.example.olpgas.browse_rooms.data.local.database.BrowseRoomDatabase
import com.example.olpgas.browse_rooms.data.remote.model.AllRoomDetails
import com.example.olpgas.browse_rooms.data.remote.model.RoomBookingStatus
import com.example.olpgas.browse_rooms.domain.util.Constants
import com.example.olpgas.core.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

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
            Log.e(TAG, "Error: ${e.message}", e)
            null
        }
    }

    suspend fun getRoomsImageForListing(ownerIds: List<String>, roomNames: List<String>) : List<String>? {
        return try {
            val urls = mutableListOf<String>()
            for(i in ownerIds.indices) {
                val bucket = SupabaseClient.client.storage.from("RoomPics")
                val files = bucket.list("${ownerIds[i]}/${roomNames[i]}")
                bucket.createSignedUrl("${ownerIds[i]}/${roomNames[i]}/${files[0].name}", Duration.INFINITE)
                val url = bucket.publicUrl("${ownerIds[i]}/${roomNames[i]}/${files[0].name}")
                urls.add(url)
            }
            return urls
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}", e)
            null
        }
    }
}