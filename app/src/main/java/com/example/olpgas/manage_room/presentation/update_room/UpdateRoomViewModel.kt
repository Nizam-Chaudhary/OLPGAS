package com.example.olpgas.manage_room.presentation.update_room

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.olpgas.browse_rooms.domain.use_case.GetLocalCacheUseCase
import com.example.olpgas.browse_rooms.domain.use_case.RefreshLocalCacheUseCase
import com.example.olpgas.core.util.ConnectivityObserver
import com.example.olpgas.manage_room.domain.use_case.update_room.UpdateAddressUseCase
import com.example.olpgas.manage_room.domain.use_case.update_room.UpdateRoomNameUseCase
import com.example.olpgas.manage_room.domain.use_case.update_room.UpdateRoomUseCases
import com.example.olpgas.view_room_details.data.local.database.entities.FullRoomDetailsLocal
import com.example.olpgas.view_room_details.domain.use_case.GetFullRoomDetailsFromLocalDBUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateRoomViewModel @Inject constructor(
    private val updateRoomUseCases: UpdateRoomUseCases
) : ViewModel(){

    lateinit var allRoomDetailsState: LiveData<FullRoomDetailsLocal>

    val connectionStatus = updateRoomUseCases.connectivityObserver.observe().asLiveData()

    fun onEvent(event: UpdateRoomEvent) {
        when(event) {
            is UpdateRoomEvent.OnCreate -> allRoomDetailsState = updateRoomUseCases.getFullRoomDetailsFromLocalDBUseCase(event.id)
            is UpdateRoomEvent.OnUpdateRoomName -> updateRoomName(event.roomName)
            is UpdateRoomEvent.OnUpdateAddress -> updateAddress(event.streetNumber, event.landMark, event.state, event.city)
            is UpdateRoomEvent.OnUpdateDeposit -> updateDeposit(event.deposit)
            is UpdateRoomEvent.OnUpdateDescription -> updateDescription(event.description)
            is UpdateRoomEvent.OnUpdateRent -> updateRent(event.rent)
            is UpdateRoomEvent.OnUpdateRoomArea -> updateRoomArea(event.roomArea)
            is UpdateRoomEvent.OnUpdateRoomType -> updateRoomType(event.roomType)
            is UpdateRoomEvent.OnUpdateShareableBy -> updateShareableBy(event.shareableBy)
            UpdateRoomEvent.OnRemoveRoom -> removeRoom()
        }
    }

    private fun updateRoomName(newRoomName: String) {
        viewModelScope.launch {
            val id = allRoomDetailsState.value?.id
            if(id != null) {
                updateRoomUseCases.updateRoomNameUseCase(id, newRoomName)
            }
        }
    }

    private fun updateAddress(
        streetNumber: String,
        landMark: String,
        state: String,
        city: String
    ) {
        viewModelScope.launch {
            val id = allRoomDetailsState.value?.id
            if(id != null) {
                updateRoomUseCases.updateAddressUseCase(id, streetNumber, landMark, state, city)
            }
        }
    }

    private fun updateRent(
        rent: Int
    ) {
        viewModelScope.launch {
            val id = allRoomDetailsState.value?.id
            if(id != null) {
                updateRoomUseCases.updateRentUseCase(id, rent)
            }
        }
    }

    private fun updateDeposit(
        deposit: Int
    ) {
        viewModelScope.launch {
            val id = allRoomDetailsState.value?.id
            if(id != null) {
                updateRoomUseCases.updateDepositUseCase(id, deposit)
            }
        }
    }

    private fun updateShareableBy(
        shareable: Int
    ) {
        viewModelScope.launch {
            val id = allRoomDetailsState.value?.id
            if(id != null) {
                updateRoomUseCases.updateShareableByUseCase(id, shareable)
            }
        }
    }

    private fun updateRoomType(
        roomType: String
    ) {
        viewModelScope.launch {
            val id = allRoomDetailsState.value?.id
            if(id != null) {
                updateRoomUseCases.updateRoomTypeUseCase(id, roomType)
            }
        }
    }

    private fun updateRoomArea(
        roomArea: Int
    ) {
        viewModelScope.launch {
            val id = allRoomDetailsState.value?.id
            if(id != null) {
                updateRoomUseCases.updateRoomAreaUseCase(id, roomArea)
            }
        }
    }

    private fun updateDescription(
        description: String
    ) {
        viewModelScope.launch {
            val id = allRoomDetailsState.value?.id
            if(id != null) {
                updateRoomUseCases.updateDescriptionUseCase(id, description)
            }
        }
    }

    private fun removeRoom() {
        viewModelScope.launch {
            val id = allRoomDetailsState.value?.id
            val roomFeatureId = allRoomDetailsState.value?.roomFeatureId

            if(id != null && roomFeatureId != null) {
                updateRoomUseCases.removeRoomUseCase(id, roomFeatureId)
            }
        }
    }
}