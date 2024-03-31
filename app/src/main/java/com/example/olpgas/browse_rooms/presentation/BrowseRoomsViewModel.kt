package com.example.olpgas.browse_rooms.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olpgas.browse_rooms.data.remote.model.AllRoomDetails
import com.example.olpgas.browse_rooms.domain.use_case.GetRoomsImageForListingUseCase
import com.example.olpgas.browse_rooms.domain.use_case.GetRoomsForListingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowseRoomsViewModel @Inject constructor(
    private val getRoomsForListingUseCase: GetRoomsForListingUseCase,
    private val getRoomsImageForListingUseCase: GetRoomsImageForListingUseCase
) : ViewModel() {

    private val _allRoomsDetailsState = MutableLiveData<List<AllRoomDetails>?>()
    val allRoomDetailsState: LiveData<List<AllRoomDetails>?> = _allRoomsDetailsState

    private val _allRoomsImageState = MutableLiveData<List<String>?>()
    val allRoomsImageState: LiveData<List<String>?> = _allRoomsImageState

    private val _allOwnerIdState = MutableLiveData<List<String>?>()
    val allOwnerIdState: LiveData<List<String>?> = _allOwnerIdState

    private val _allRoomNameState = MutableLiveData<List<String>?>()
    val allRoomNameState: LiveData<List<String>?> = _allRoomNameState

    fun onEvent(event: BrowseRoomsEvent) {
        when(event) {
            BrowseRoomsEvent.OnCreate -> {
                getAllRoomsData()
            }
            BrowseRoomsEvent.OnRefresh -> {
                getAllRoomsData()
            }
        }
    }

    private fun getAllRoomsData() {
        viewModelScope.launch {
            _allRoomsDetailsState.value = getRoomsForListingUseCase()
            val allRoomsDetails = allRoomDetailsState.value
            val allOwnerId = mutableListOf<String>()
            val allRoomName = mutableListOf<String>()
            if(allRoomsDetails != null) {
                for(i in allRoomsDetails) {
                    allOwnerId.add(i.ownerId)
                    allRoomName.add(i.roomName)
                }
            }

            _allRoomsImageState.value = getRoomsImageForListingUseCase(
                allOwnerId,allRoomName
            )
        }
    }
}