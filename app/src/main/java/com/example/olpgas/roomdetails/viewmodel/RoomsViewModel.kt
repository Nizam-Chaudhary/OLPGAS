package com.example.olpgas.roomdetails.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olpgas.auth.data.network.SupabaseClient.client
import com.example.olpgas.roomdetails.data.model.AllRoomsDetails
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class RoomsViewModel : ViewModel() {

    private val _allRoomsDetails = MutableLiveData<List<AllRoomsDetails>>()
    val allRoomsDetails: LiveData<List<AllRoomsDetails>> = _allRoomsDetails

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

    fun fetchDisplayImages() {
        viewModelScope.launch {

        }
    }
}