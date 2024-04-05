package com.example.olpgas.manage_room.data.remote

import android.util.Log
import com.example.olpgas.core.data.remote.SupabaseClient
import com.example.olpgas.core.util.Resource
import com.example.olpgas.manage_room.data.remote.model.RoomDetails
import com.example.olpgas.manage_room.data.remote.model.RoomMaster
import com.example.olpgas.manage_room.domain.util.Constants
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import kotlinx.serialization.Serializable
import kotlin.time.Duration

class SupabaseManageRoom {
    companion object {
        const val TAG = "Supabase Manage Room"
    }
    suspend fun insertRoomDetails(roomDetails: RoomDetails) : Int? {
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

    suspend fun insertRoomMaster(roomMaster: RoomMaster) : Int? {
        return try{
            val id = SupabaseClient.client.postgrest.from(Constants.ROOM_MASTER_TABLE)
                .insert(roomMaster) {
                    select(Columns.list(Constants.COL_ID_ROOM_MASTER))
                }.decodeSingle<Id>().id
            id
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
            null
        }
    }

    suspend fun uploadImages(ownerId: String, id: Int, images: List<ByteArray>){
        try {
            val bucket = SupabaseClient.client.storage.from(Constants.ROOM_PICS_BUCKET)
            for(i in images.indices) {
                bucket.upload("$ownerId/$id/img$i.jpeg", images[i])
                bucket.createSignedUrl("$ownerId/$id/img$i.jpeg", Duration.INFINITE)
            }
            Resource.Success(Unit)
        }catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Error: ${e.message}")
        }
    }

    suspend fun updateRoomName(id: Int, newRoomName: String) {
        try {
            SupabaseClient.client.postgrest.from(Constants.ROOM_MASTER_TABLE)
                .update({
                    set(Constants.COL_ROOM_NAME, newRoomName)
                }) {
                    filter {
                        eq(Constants.COL_ID_ROOM_MASTER, id)
                    }
                }
        } catch(e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Error: ${e.message}")
        }
    }

    suspend fun updateAddress(
        id: Int,
        streetNumber: String,
        landMark: String,
        state: String,
        city: String
    ) {
        try {
            SupabaseClient.client.postgrest.from(Constants.ROOM_MASTER_TABLE)
                .update({
                    set(Constants.COL_STREET_NUMBER, streetNumber)
                    set(Constants.COL_LANDMARK, landMark)
                    set(Constants.COL_STATE, state)
                    set(Constants.COL_CITY, city)
                }) {
                    filter {
                        eq(Constants.COL_ID_ROOM_MASTER, id)
                    }
                }
        } catch(e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Error: ${e.message}")
        }
    }

    suspend fun updateRent(
        id: Int,
        rent: Int
    ) {
        try {
            SupabaseClient.client.postgrest.from(Constants.ROOM_DETAILS_TABLE)
                .update({
                    set(Constants.COL_RENT_AMOUNT, rent)
                }) {
                    filter {
                        eq(Constants.COL_ID_ROOM_DETAILS, id)
                    }
                }
        } catch(e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Error: ${e.message}")
        }
    }

    suspend fun updateDeposit(
        id: Int,
        deposit: Int
    ) {
        try {
            SupabaseClient.client.postgrest.from(Constants.ROOM_DETAILS_TABLE)
                .update({
                    set(Constants.COL_DEPOSIT, deposit)
                }) {
                    filter {
                        eq(Constants.COL_ID_ROOM_DETAILS, id)
                    }
                }
        } catch(e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Error: ${e.message}")
        }
    }

    suspend fun updateShareableBy(
        id: Int,
        shareable: Int
    ) {
        try {
            SupabaseClient.client.postgrest.from(Constants.ROOM_DETAILS_TABLE)
                .update({
                    set(Constants.COL_SHAREABLE, shareable)
                }) {
                    filter {
                        eq(Constants.COL_ID_ROOM_DETAILS, id)
                    }
                }
        } catch(e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Error: ${e.message}")
        }
    }

    suspend fun updateRoomType(
        id: Int,
        roomType: String
    ) {
        try {
            SupabaseClient.client.postgrest.from(Constants.ROOM_DETAILS_TABLE)
                .update({
                    set(Constants.COL_ROOM_TYPE, roomType)
                }) {
                    filter {
                        eq(Constants.COL_ID_ROOM_DETAILS, id)
                    }
                }
        } catch(e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Error: ${e.message}")
        }
    }

    suspend fun updateRoomArea(
        id: Int,
        roomArea: Int
    ) {
        try {
            SupabaseClient.client.postgrest.from(Constants.ROOM_DETAILS_TABLE)
                .update({
                    set(Constants.COL_ROOM_AREA, roomArea)
                }) {
                    filter {
                        eq(Constants.COL_ID_ROOM_DETAILS, id)
                    }
                }
        } catch(e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Error: ${e.message}")
        }
    }

    suspend fun updateDescription(
        id: Int,
        description: String
    ) {
        try {
            SupabaseClient.client.postgrest.from(Constants.ROOM_DETAILS_TABLE)
                .update({
                    set(Constants.COL_ROOM_DESCRIPTION, description)
                }) {
                    filter {
                        eq(Constants.COL_ID_ROOM_DETAILS, id)
                    }
                }
        } catch(e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Error: ${e.message}")
        }
    }

    suspend fun removeRoom(
       id: Int,
       roomFeatureId: Int
    ) {
        try {
            SupabaseClient.client.postgrest.from(Constants.ROOM_DETAILS_TABLE)
                .delete() {
                    filter {
                        eq(Constants.COL_ID_ROOM_DETAILS, roomFeatureId)
                    }
                }

            SupabaseClient.client.postgrest.from(Constants.ROOM_MASTER_TABLE)
                .delete() {
                    filter {
                        eq(Constants.COL_ID_ROOM_MASTER, id)
                    }
                }
        } catch(e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Error: ${e.message}")
        }
    }

    @Serializable
    data class Id(val id: Int)
}