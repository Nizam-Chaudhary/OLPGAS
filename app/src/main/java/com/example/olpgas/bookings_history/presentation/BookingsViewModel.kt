package com.example.olpgas.bookings_history.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olpgas.bookings_history.data.local.database.entity.RoomBookingLocal
import com.example.olpgas.bookings_history.domain.use_case.GetBookingsDataForUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingsViewModel @Inject constructor(
    private val getBookingsDataForUserUseCase: GetBookingsDataForUserUseCase
) : ViewModel() {
    lateinit var allBookings: LiveData<List<RoomBookingLocal>>

    fun onEvent(event: BookingEvent) {
        when(event) {
            BookingEvent.OnCreate -> viewModelScope.launch {
                allBookings = getBookingsDataForUserUseCase()
            }
        }
    }
}