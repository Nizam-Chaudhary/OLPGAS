package com.example.olpgas.manage_room.data.remote

import android.util.Log
import com.example.olpgas.core.data.remote.SupabaseClient
import com.example.olpgas.core.util.Resource
import com.example.olpgas.core.util.SimpleResource
import com.example.olpgas.manage_room.data.remote.model.RoomDetails
import com.example.olpgas.manage_room.data.remote.model.RoomMaster
import com.example.olpgas.manage_room.domain.util.Constants
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import kotlinx.serialization.Serializable

class SupabaseManageRoom {
    companion object {
        const val TAG = "Supabase Manage Room"
    }
    suspend fun upsertRoomDetails(roomDetails: RoomDetails) : Int? {
        return try{
            SupabaseClient.client.postgrest.from(Constants.ROOM_DETAILS_TABLE)
                .insert(roomDetails) {
                    select(Columns.list(Constants.COL_ID_ROOM_DETAILS))
                }.decodeSingle<Id>().id
        }catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Error: ${e.message}")
            null
        }
    }

    suspend fun upsertRoomMaster(roomMaster: RoomMaster) : SimpleResource {
        return try{
            SupabaseClient.client.postgrest.from(Constants.ROOM_MASTER_TABLE)
                .insert(roomMaster)
            Resource.Success(Unit)
        }catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Error: ${e.message}")
            SupabaseClient.client.postgrest.from(Constants.ROOM_DETAILS_TABLE)
                .delete {
                    filter {
                        eq(Constants.COL_ID_ROOM_DETAILS, roomMaster.roomFeatureId)
                    }
                }

            val bucket = SupabaseClient.client.storage.from(Constants.ROOM_PICS_BUCKET)
            val files = bucket.list()
            for(file in files) {
                bucket.delete("${roomMaster.ownerId}/${roomMaster.roomName}/${file.name}")
            }
            Resource.Error("Error Posting Room Details")
        }
    }

    suspend fun uploadImages(ownerId: String, roomName: String, images: List<ByteArray>){
        try {
            val bucket = SupabaseClient.client.storage.from(Constants.ROOM_PICS_BUCKET)
            for(i in images.indices) {
                bucket.upload("$ownerId/$roomName/img$i.jpeg", images[i])
            }
            Resource.Success(Unit)
        }catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Error: ${e.message}")
        }
    }

    @Serializable
    data class Id(val id: Int)
}