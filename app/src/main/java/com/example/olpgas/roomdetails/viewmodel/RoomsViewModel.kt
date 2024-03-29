package com.example.olpgas.roomdetails.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olpgas.auth.data.remote.SupabaseClient.client
import com.example.olpgas.profile.data.model.UserName
import com.example.olpgas.roomdetails.data.model.AllRoomsDetails
import com.example.olpgas.roomdetails.data.model.Filter
import com.example.olpgas.roomdetails.data.model.FullRoomDetails
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch

class RoomsViewModel : ViewModel() {

    private val _allRoomsDetails = MutableLiveData<List<AllRoomsDetails>>()
    val allRoomsDetails: LiveData<List<AllRoomsDetails>> = _allRoomsDetails

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _fullRoomDetails = MutableLiveData<FullRoomDetails>()
    val fullRoomDetails: LiveData<FullRoomDetails> = _fullRoomDetails

    fun fetchAllRooms(filter: Filter? = null) {
        viewModelScope.launch {
            try{
                val allRoomsDetails = client.postgrest.from("AllRooms")
                    .select{
                        filter{
                            if (filter != null) {
                                if(filter.city != null) {
                                    eq("city", filter.city)
                                }
                                if(filter.minRentAmount != null) {
                                    gte("rentAmount", filter.minRentAmount)
                                }
                                if(filter.maxRentAmount != null) {
                                    gte("rentAmount", filter.maxRentAmount)
                                }
                            }
                        }
                    }.decodeList<AllRoomsDetails>()

                _allRoomsDetails.value = allRoomsDetails
            } catch(e: Exception) {
                Log.e("Room","Error: ${e.message}")
            }
        }
    }
    fun getUserName() {
        viewModelScope.launch {
            try {
                _userName.value = client.postgrest.from("UserDetails")
                    .select(Columns.list("userName")).decodeSingle<UserName>().userName
            } catch(e: Exception) {
                Log.e("GetUserName","Error: ${e.message}")
            }
        }
    }



    fun getFullRoomDetails(roomId: Int) {
        viewModelScope.launch {
            try {
                _fullRoomDetails.value = client.postgrest.from("FullRoomDetails")
                    .select {
                        filter {
                            eq("id",roomId)
                        }
                    }.decodeSingle<FullRoomDetails>()

                Log.d("Room", _fullRoomDetails.value.toString())
            }catch (e: Exception) {
                Log.d("Room", "Error: ${e.message}")
            }
        }
    }

    fun removeRoomDetails(roomMasterId: Int, roomDetailsId: Int, ownerId: String, roomName: String) {
        viewModelScope.launch {
            try {
                client.postgrest.from("RoomMaster").delete {
                    filter {
                        eq("id", roomMasterId)
                    }
                }
                client.postgrest.from("RoomDetails").delete {
                    filter {
                        eq("id", roomDetailsId)
                    }
                }
                val bucket = client.storage.from("RoomPics")
                val files = bucket.list("$ownerId/$roomName")
                for(file in files) {
                    bucket.delete("$ownerId/$roomName/${file.name}")
                }
            }catch (e: Exception) {
                Log.e("Room","Error: ${e.message}")
            }
        }
    }
}