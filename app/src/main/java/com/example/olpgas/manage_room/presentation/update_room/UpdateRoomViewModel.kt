package com.example.olpgas.manage_room.presentation.update_room

import android.util.Log
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
            is UpdateRoomEvent.AddAmenity -> addAmenity(event.amenity)
            is UpdateRoomEvent.AddSuitableFor -> addSuitableFor(event.suitableFor)
            is UpdateRoomEvent.RemoveAmenity -> removeAmenity(event.amenity)
            is UpdateRoomEvent.RemoveSuitableFor -> removeSuitableFor(event.suitableFor)
            is UpdateRoomEvent.AddImage -> addImage(event.image)
            is UpdateRoomEvent.RemoveImage -> {
                removeImage(event.position)
            }
        }
    }

    private fun addImage(imageByteArray: ByteArray) {
        viewModelScope.launch {
            val id = allRoomDetailsState.value?.id
            val ownerId = allRoomDetailsState.value?.ownerId

            if(id != null && ownerId != null) {
                updateRoomUseCases.addImageUseCase(ownerId, id, imageByteArray)
                updateRoomUseCases.refreshLocalCacheUseCase()
            }
        }
    }

    private fun removeImage(position: Int) {
        viewModelScope.launch {
            val id = allRoomDetailsState.value?.id
            val ownerId = allRoomDetailsState.value?.ownerId
            val url = allRoomDetailsState.value?.urls?.get(position)
            val imageName = url?.substring(url.lastIndexOf("/") + 1)

            if(id != null && ownerId != null && imageName != null) {
                updateRoomUseCases.removeImageUseCase(ownerId!!, id!!, imageName!!)
                updateRoomUseCases.refreshLocalCacheUseCase()
            }
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
            val roomFeatureId = allRoomDetailsState.value?.roomFeatureId
            if(id != null && roomFeatureId != null) {
                updateRoomUseCases.updateRentUseCase(id, roomFeatureId, rent)
            }
        }
    }

    private fun updateDeposit(
        deposit: Int
    ) {
        viewModelScope.launch {
            val id = allRoomDetailsState.value?.id
            val roomFeatureId = allRoomDetailsState.value?.roomFeatureId
            if(id != null && roomFeatureId != null) {
                updateRoomUseCases.updateDepositUseCase(id, roomFeatureId, deposit)
            }
        }
    }

    private fun updateShareableBy(
        shareable: Int
    ) {
        viewModelScope.launch {
            val id = allRoomDetailsState.value?.id
            val roomFeatureId = allRoomDetailsState.value?.roomFeatureId
            if(id != null && roomFeatureId != null) {
                updateRoomUseCases.updateShareableByUseCase(id, roomFeatureId, shareable)
            }
        }
    }

    private fun updateRoomType(
        roomType: String
    ) {
        viewModelScope.launch {
            val id = allRoomDetailsState.value?.id
            val roomFeatureId = allRoomDetailsState.value?.roomFeatureId
            if(id != null && roomFeatureId != null) {
                updateRoomUseCases.updateRoomTypeUseCase(id, roomFeatureId, roomType)
            }
        }
    }

    private fun updateRoomArea(
        roomArea: Int
    ) {
        viewModelScope.launch {
            val id = allRoomDetailsState.value?.id
            val roomFeatureId = allRoomDetailsState.value?.roomFeatureId
            if(id != null && roomFeatureId != null) {
                updateRoomUseCases.updateRoomAreaUseCase(id, roomFeatureId, roomArea)
            }
        }
    }

    private fun updateDescription(
        description: String
    ) {
        viewModelScope.launch {
            val id = allRoomDetailsState.value?.id
            val roomFeatureId = allRoomDetailsState.value?.roomFeatureId
            if(id != null && roomFeatureId != null) {
                Log.d("Update Description", "ID : $id")
                updateRoomUseCases.updateDescriptionUseCase(id, roomFeatureId, description)
            }
        }
    }

    private fun removeRoom() {
        viewModelScope.launch {
            val id = allRoomDetailsState.value?.id
            val roomFeatureId = allRoomDetailsState.value?.roomFeatureId
            val ownerId = allRoomDetailsState.value?.ownerId

            if(id != null && roomFeatureId != null && ownerId != null) {
                updateRoomUseCases.removeRoomUseCase(id, roomFeatureId, ownerId)
            }
        }
    }

    private fun addAmenity(
        amenity: String
    ) {
        viewModelScope.launch {
            val id = allRoomDetailsState.value?.id
            val roomFeatureId = allRoomDetailsState.value?.roomFeatureId

            val newAmenities = mutableListOf<String>()
            val oldAmenities = allRoomDetailsState.value?.features
            oldAmenities?.let {
                for(oldAmenity in oldAmenities) {
                    newAmenities.add(oldAmenity)
                }
                newAmenities.add(amenity)

                if(id != null && roomFeatureId != null) {
                    updateRoomUseCases.updateAmenityUseCase(id, roomFeatureId, newAmenities)
                }
            }
        }
    }

    private fun removeAmenity(
        amenity: String
    ) {
        viewModelScope.launch {
            val id = allRoomDetailsState.value?.id
            val roomFeatureId = allRoomDetailsState.value?.roomFeatureId

            val newAmenities = mutableListOf<String>()
            val oldAmenities = allRoomDetailsState.value?.features
            oldAmenities?.let {
                for(oldAmenity in oldAmenities) {
                    newAmenities.add(oldAmenity)
                }
                newAmenities.remove(amenity)

                if(id != null && roomFeatureId != null) {
                    updateRoomUseCases.updateAmenityUseCase(id, roomFeatureId, newAmenities)
                }
            }
        }
    }

    private fun addSuitableFor(
        suitableFor: String
    ) {
        viewModelScope.launch {
            val id = allRoomDetailsState.value?.id
            val roomFeatureId = allRoomDetailsState.value?.roomFeatureId

            val newSuitableFors = mutableListOf<String>()
            val oldSuitableFors = allRoomDetailsState.value?.suitableFor
            oldSuitableFors?.let {
                for(oldSuitableFor in oldSuitableFors) {
                    newSuitableFors.add(oldSuitableFor)
                }
                newSuitableFors.add(suitableFor)

                if(id != null && roomFeatureId != null) {
                    updateRoomUseCases.updateSuitableForUseCase(id, roomFeatureId, newSuitableFors)
                }
            }
        }
    }

    private fun removeSuitableFor(
        suitableFor: String
    ) {
        viewModelScope.launch {
            val id = allRoomDetailsState.value?.id
            val roomFeatureId = allRoomDetailsState.value?.roomFeatureId

            val newSuitableFors = mutableListOf<String>()
            val oldSuitableFors = allRoomDetailsState.value?.suitableFor
            oldSuitableFors?.let {
                for(oldSuitableFor in oldSuitableFors) {
                    newSuitableFors.add(oldSuitableFor)
                }
                newSuitableFors.remove(suitableFor)

                if(id != null && roomFeatureId != null) {
                    updateRoomUseCases.updateSuitableForUseCase(id, roomFeatureId, newSuitableFors)
                }
            }
        }
    }
}