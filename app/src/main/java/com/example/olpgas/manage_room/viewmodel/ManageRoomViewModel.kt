package com.example.olpgas.manage_room.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olpgas.auth.data.network.SupabaseClient.client
import com.example.olpgas.manage_room.model.RoomDetails
import com.example.olpgas.manage_room.model.RoomMaster
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class ManageRoomViewModel : ViewModel() {

    fun uploadRoomDetails(roomDetails: RoomDetails, roomMaster: RoomMaster, images: List<ByteArray>) {
        viewModelScope.launch {
            try {
                val id = client.postgrest.from("RoomDetails")
                    .insert(roomDetails){
                        select(Columns.list("id"))
                    }
                    .decodeSingle<Id>().id
                Log.d("ManageRoom", "room details data upload IN Progress")

                roomMaster.roomFeatureId = id
                client.postgrest.from("RoomMaster")
                    .insert(roomMaster)

                Log.d("ManageRoom", "room master data upload IN Progress")

                val bucket = client.storage.from("RoomPics")

                val ownerId = client.auth.currentUserOrNull()?.id

                for(i in images.indices) {
                    Log.d("ManageRoom", "image upload IN Progress")
                    bucket.upload("${ownerId}/${roomMaster.roomName}/img$i.jpg", images[i],upsert = false)
                }
            }catch (e: Exception) {
                e.printStackTrace()
                Log.d("ManageRoom", "${e.message}")
            }
        }
    }

    @Serializable
    data class Id(val id: Int)
}