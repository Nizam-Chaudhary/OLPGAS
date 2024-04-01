package com.example.olpgas.view_room_details.data.remote.model

import android.util.Log
import com.example.olpgas.core.data.remote.SupabaseClient
import com.example.olpgas.view_room_details.domain.util.Constants
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlin.time.Duration

class SupabaseRoomDetails {
    companion object {
        const val TAG = "Supabase Room Full Details"
    }
    suspend fun getFullRoomDetails(id: Int) : FullRoomDetails?{
        return try {
            SupabaseClient.client.postgrest.from(Constants.FULL_ROOM_DETAILS_TABLE)
                .select() {
                    filter {
                        eq(Constants.COL_ID, id)
                    }
                }.decodeSingle<FullRoomDetails>()
        }catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
            null
        }
    }

    suspend fun getFullRoomDetailsImages(ownerId: String, roomName: String) : List<String>? {
        return try {
            val urls = mutableListOf<String>()
            val bucket = SupabaseClient.client.storage.from("RoomPics")
            val files = bucket.list("$ownerId/$roomName")
            for(file in files) {
                bucket.createSignedUrl("$ownerId/$roomName/${file.name}", Duration.INFINITE)
                val url = bucket.publicUrl("$ownerId/$roomName/$${file.name}")
                urls.add(url)
            }
            return urls
        }catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
            null
        }
    }
}