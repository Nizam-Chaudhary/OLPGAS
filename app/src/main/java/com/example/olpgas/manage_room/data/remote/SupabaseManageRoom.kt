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

class SupabaseManageRoom {
    companion object {
        const val TAG = "Supabase Manage Room"
    }
    suspend fun upsertRoomDetails(roomDetails: RoomDetails) : Int? {
        return try{
            SupabaseClient.client.postgrest.from(Constants.ROOM_DETAILS_TABLE)
                .upsert(roomDetails) {
                    select(Columns.list(Constants.COL_ID_ROOM_DETAILS))
                }.decodeSingle<RoomDetails>().id
        }catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
            null
        }
    }

    suspend fun upsertRoomMaster(roomMaster: RoomMaster) : SimpleResource {
        return try{
            SupabaseClient.client.postgrest.from(Constants.ROOM_MASTER_TABLE)
                .upsert(roomMaster)
            Resource.Success(Unit)
        }catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
            SupabaseClient.client.postgrest.from(Constants.ROOM_DETAILS_TABLE)
                .delete {
                    filter {
                        eq(Constants.COL_ID_ROOM_DETAILS, roomMaster.roomFeatureId)
                    }
                }
            Resource.Error("Error Posting Room Details")
        }
    }
}