package com.example.olpgas.roomdetails.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olpgas.auth.data.network.SupabaseClient.client
import com.example.olpgas.profile.data.model.UserName
import com.example.olpgas.roomdetails.data.model.AllRoomsDetails
import com.example.olpgas.roomdetails.data.model.FullRoomDetails
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch

class RoomsViewModel : ViewModel() {

    private val _allRoomsDetails = MutableLiveData<List<AllRoomsDetails>>()
    val allRoomsDetails: LiveData<List<AllRoomsDetails>> = _allRoomsDetails

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _fullRoomDetails = MutableLiveData<FullRoomDetails>()
    val fullRoomDetails: LiveData<FullRoomDetails> = _fullRoomDetails

    fun fetchAllRooms() {
        viewModelScope.launch {
            try{
                val allRoomsDetails = client.postgrest.from("AllRooms")
                    .select().decodeList<AllRoomsDetails>()

                _allRoomsDetails.value = allRoomsDetails
            } catch(e: Exception) {
                Log.e("Room","Error: ${e.message}")
            }
        }
    }

    fun getUserName() {
        viewModelScope.launch {
            try {
                _userName.value = client.postgrest.from("Users")
                    .select(Columns.list("userName")).decodeSingle<UserName>().userName
            } catch(e: Exception) {
                Log.e("Room","Error: ${e.message}")
            }
        }
    }

    fun getFullRoomDetails(roomId: Int) {
        viewModelScope.launch {
            try {
                _fullRoomDetails.value = client.postgrest.from("FullRoomDetails")
                    .select() {
                        filter {
                            eq("id",roomId)
                        }
                    }.decodeSingle<FullRoomDetails>()
            }catch (e: Exception) {
                Log.d("Room", "Error: ${e.message}")
            }
        }
    }
}