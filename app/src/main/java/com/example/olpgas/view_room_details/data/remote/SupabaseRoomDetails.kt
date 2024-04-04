package com.example.olpgas.view_room_details.data.remote

import android.util.Log
import com.example.olpgas.browse_rooms.data.remote.SupabaseListRooms
import com.example.olpgas.core.data.remote.SupabaseClient
import com.example.olpgas.view_room_details.data.remote.model.FullRoomDetails
import com.example.olpgas.view_room_details.domain.util.Constants
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlin.time.Duration

class SupabaseRoomDetails {
    companion object {
        const val TAG = "Supabase Room Full Details"
    }
    suspend fun getAllFullRoomDetails() : List<FullRoomDetails>? {
        return try {
            SupabaseClient.client.postgrest.from(Constants.FULL_ROOM_DETAILS_TABLE)
                .select().decodeList<FullRoomDetails>()
        }catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
            null
        }
    }

    suspend fun getFullRoomDetailsImages(ownerId: String, id: Int) : List<String>? {
        return try {
            val urls = mutableListOf<String>()
            val bucket = SupabaseClient.client.storage.from(Constants.ROOM_PICS_BUCKET)
            val files = bucket.list("${ownerId}/${id}")
            for(file in files) {
                val url = bucket.publicUrl("${ownerId}/${id}/${file.name}")
                urls.add(url)
            }
            return urls
        } catch (e: Exception) {
            Log.e(SupabaseListRooms.TAG, "Error: ${e.message}")
            null
        }
    }
}