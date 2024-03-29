package com.example.olpgas.manage_room.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olpgas.core.data.remote.SupabaseClient.client
import com.example.olpgas.manage_room.model.RoomDetails
import com.example.olpgas.manage_room.model.RoomMaster
import com.example.olpgas.manage_room.model.WorkState
import com.example.olpgas.roomdetails.data.model.AllRoomsDetails
import com.example.olpgas.roomdetails.data.model.FullRoomDetails
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class ManageRoomViewModel : ViewModel() {
    private val _myRoomDetails = MutableLiveData<List<AllRoomsDetails>>()
    val myRoomDetails: LiveData<List<AllRoomsDetails>> = _myRoomDetails

    private val _addRoomStatus = MutableLiveData<WorkState>()
    val addRoomStatus: LiveData<WorkState> = _addRoomStatus

    private val _fullRoomDetails = MutableLiveData<FullRoomDetails>()
    val fullRoomDetails: LiveData<FullRoomDetails> = _fullRoomDetails

    fun uploadRoomDetails(roomDetails: RoomDetails, roomMaster: RoomMaster, images: List<ByteArray>) {
        viewModelScope.launch {
            try {
                _addRoomStatus.value = WorkState.Loading
                val id = client.postgrest.from("RoomDetails")
                    .insert(roomDetails){
                        select(Columns.list("id"))
                    }
                    .decodeSingle<Id>().id

                roomMaster.roomFeatureId = id
                client.postgrest.from("RoomMaster")
                    .insert(roomMaster)

                val bucket = client.storage.from("RoomPics")

                val ownerId = client.auth.currentUserOrNull()?.id

                for(i in images.indices) {
                    Log.d("ManageRoom", "image upload IN Progress")
                    bucket.upload("${ownerId}/${roomMaster.roomName}/img$i.jpg", images[i],upsert = false)
                }
                _addRoomStatus.value = WorkState.Success("Room Details Added Successfully")
            }catch (e: Exception) {
                _addRoomStatus.value = WorkState.Success("Error Adding Room Details")
                Log.d("Room", "${e.message}")
            }
        }
    }

    fun updateRoomDetails(roomDetails: RoomDetails, roomMaster: RoomMaster, oldRoomName: String) {
        viewModelScope.launch {
            _addRoomStatus.value = WorkState.Loading
            try {
                client.postgrest.from("RoomMaster")
                    .update({
                        set("roomName", roomMaster.roomName)
                        set("roomNumber", roomMaster.roomNumber)
                        set("streetNumber", roomMaster.streetNumber)
                        set("landMark", roomMaster.landMark)
                        set("city", roomMaster.city)
                        set("state", roomMaster.state)
                    }) {
                        filter {
                            eq("id", roomMaster.id!!)
                        }
                    }
                val ownerId = client.auth.currentUserOrNull()?.id ?: ""
                val bucket = client.storage.from("RoomPics")
                bucket.move("$ownerId/$oldRoomName", "$ownerId/${roomMaster.roomName}")
                _addRoomStatus.value = WorkState.Success("Room Details Updated Successfully")
            }catch (e: Exception){
                _addRoomStatus.value = WorkState.Success("Error Updating Room Details")
                Log.d("Room", "${e.message}")
            }
        }
    }

    fun fetchMyRooms() {
        viewModelScope.launch {
            try{
                val ownerId = client.auth.currentUserOrNull()?.id ?: ""
                val allRoomsDetails = client.postgrest.from("AllRooms")
                    .select(){
                        filter {
                            eq("ownerId", ownerId)
                        }
                    }.decodeList<AllRoomsDetails>()

                _myRoomDetails.value = allRoomsDetails
            } catch(e: Exception) {
                Log.e("Room","Error: ${e.message}")
            }
        }
    }

    fun fetchFullRoomDetails(id: Int) {
        viewModelScope.launch {
            try {
                _fullRoomDetails.value = client.postgrest.from("FullRoomDetails").select {
                    filter {
                        eq("id", id)
                    }
                }.decodeSingle<FullRoomDetails>()
            }catch (e: Exception) {
                Log.e("Room", "Error: ${e.message}")
            }
        }
    }

    @Serializable
    data class Id(val id: Int)
}