package com.example.olpgas.manage_room.presentation.owned_rooms

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.manage_room.domain.use_case.GetAllOwnedRoomsUseCase
import com.example.olpgas.manage_room.domain.use_case.update_room.RemoveRoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnedRoomsViewModel @Inject constructor(
    private val getAllOwnedRoomsUseCase: GetAllOwnedRoomsUseCase,
    private val removeRoomUseCase: RemoveRoomUseCase
) : ViewModel() {

    var allRoomDetailsState: LiveData<List<AllRoomsDetailsLocal>>? = getAllOwnedRoomsUseCase()

    fun onEvent(event: OwnedRoomEvents) {
        when(event) {
            OwnedRoomEvents.OnCreate -> {
                allRoomDetailsState = getAllOwnedRoomsUseCase()
            }
        }
    }
}