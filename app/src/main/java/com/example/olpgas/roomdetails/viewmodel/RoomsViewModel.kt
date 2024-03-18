package com.example.olpgas.roomdetails.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olpgas.auth.data.network.SupabaseClient
import com.example.olpgas.auth.data.network.SupabaseClient.client
import com.example.olpgas.roomdetails.data.model.AllRoomsDetails
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch
import kotlin.reflect.jvm.internal.impl.descriptors.deserialization.PlatformDependentDeclarationFilter.All

class RoomsViewModel : ViewModel() {

    private val _allRoomsDetails = MutableLiveData<List<AllRoomsDetails>>()
    val allRoomsDetails: LiveData<List<AllRoomsDetails>> = _allRoomsDetails

    fun fetchAllRooms() {
        viewModelScope.launch {
            try{
                val allRoomsDetails = client.postgrest.from("AllRooms")
                    .select().decodeList<AllRoomsDetails>()

                for(room in allRoomsDetails) {
                    val bucket = client.storage.from("RoomPics")
                    val mainBytes = bucket.downloadPublic("${room.ownerId}/main.jpg")
                    room.images?.add(mainBytes)

                    for(i in 1..3) {
                        val subBytes = bucket.downloadPublic("${room.ownerId}/sub$i.jpg")
                        room.images?.add(subBytes)
                    }
                }

                _allRoomsDetails.value = allRoomsDetails
            } catch(e: Exception) {
                Log.e("Room","Error: ${e.message}")
            }
        }
    }
}