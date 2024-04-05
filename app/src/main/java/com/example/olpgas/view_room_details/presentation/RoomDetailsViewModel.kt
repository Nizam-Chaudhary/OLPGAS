package com.example.olpgas.view_room_details.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.core.util.ConnectivityObserver
import com.example.olpgas.view_room_details.data.local.database.entities.FullRoomDetailsLocal
import com.example.olpgas.view_room_details.domain.use_case.GetFullRoomDetailsFromLocalDBUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoomDetailsViewModel @Inject constructor(
    private val getFullRoomDetailsFromLocalDBUseCase: GetFullRoomDetailsFromLocalDBUseCase,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {
    lateinit var allRoomDetailsState: LiveData<FullRoomDetailsLocal>

    val connectionStatus = connectivityObserver.observe().asLiveData()
    fun onEvent(event: RoomDetailsEvent) {
        when(event) {
            is RoomDetailsEvent.OnCreate -> allRoomDetailsState = getFullRoomDetailsFromLocalDBUseCase(event.id)
        }
    }
}