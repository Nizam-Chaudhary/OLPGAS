package com.example.olpgas.manage_room.presentation.booking_history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olpgas.bookings_history.data.local.database.entity.RoomBookingLocal
import com.example.olpgas.bookings_history.domain.use_case.GetBookingsDataForUserUseCase
import com.example.olpgas.manage_room.domain.use_case.GetBookingsDataForOwnerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomBookingViewModel @Inject constructor(
    private val getBookingsDataForOwnerUseCase: GetBookingsDataForOwnerUseCase
) : ViewModel() {
    lateinit var allBookings: LiveData<List<RoomBookingLocal>>

    fun onEvent(event: RoomBookingEvent) {
        when(event) {
            RoomBookingEvent.OnCreate -> viewModelScope.launch {
                allBookings = getBookingsDataForOwnerUseCase()
            }
        }
    }
}