package com.example.olpgas.manage_room.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olpgas.auth.data.network.SupabaseClient.client
import com.example.olpgas.manage_room.model.RoomDetails
import com.example.olpgas.manage_room.model.RoomMaster
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class ManageRoomViewModel : ViewModel() {

    fun uploadRoomDetails(roomDetails: RoomDetails, roomMaster: RoomMaster, images: List<ByteArray>) {
        viewModelScope.launch {
            client.postgrest.from("RoomDetails")
                .insert(roomDetails)
        }
    }


}