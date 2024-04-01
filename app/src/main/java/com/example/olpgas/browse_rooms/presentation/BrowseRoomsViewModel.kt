package com.example.olpgas.browse_rooms.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.browse_rooms.domain.use_case.GetAllRoomDetailsFromLocalDBUseCase
import com.example.olpgas.browse_rooms.domain.use_case.RefreshLocalCacheUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowseRoomsViewModel @Inject constructor(
    private val getAllRoomDetailsFromLocalDBUseCase: GetAllRoomDetailsFromLocalDBUseCase,
    private val refreshLocalCacheUseCase: RefreshLocalCacheUseCase
) : ViewModel() {

    lateinit var allRoomDetailsState: LiveData<List<AllRoomsDetailsLocal>>

    fun onEvent(event: BrowseRoomsEvent) {
        when(event) {
            BrowseRoomsEvent.OnCreate -> {
                viewModelScope.launch {
                    allRoomDetailsState = getAllRoomDetailsFromLocalDBUseCase()
                }
            }
            BrowseRoomsEvent.OnRefresh -> {
                viewModelScope.launch {
                    refreshLocalCacheUseCase()
                    allRoomDetailsState = getAllRoomDetailsFromLocalDBUseCase()
                }
            }
        }
    }
}