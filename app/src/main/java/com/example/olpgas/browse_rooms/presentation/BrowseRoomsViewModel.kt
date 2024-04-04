package com.example.olpgas.browse_rooms.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.browse_rooms.domain.use_case.GetAllRoomDetailsFromLocalDBUseCase
import com.example.olpgas.browse_rooms.domain.use_case.GetLocalCacheUseCase
import com.example.olpgas.browse_rooms.domain.use_case.RefreshLocalCacheUseCase
import com.example.olpgas.core.util.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowseRoomsViewModel @Inject constructor(
    private val getAllRoomDetailsFromLocalDBUseCase: GetAllRoomDetailsFromLocalDBUseCase,
    private val getLocalCacheUseCase: GetLocalCacheUseCase,
    private val refreshLocalCacheUseCase: RefreshLocalCacheUseCase,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {

    var allRoomDetailsState: LiveData<List<AllRoomsDetailsLocal>> = getAllRoomDetailsFromLocalDBUseCase()

    val connectionStatus = connectivityObserver.observe().asLiveData()

    fun onEvent(event: BrowseRoomsEvent) {
        when(event) {
            BrowseRoomsEvent.OnCreate -> {
                viewModelScope.launch {
                    getLocalCacheUseCase()
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