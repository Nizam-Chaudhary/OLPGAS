package com.example.olpgas.view_room_details.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.olpgas.bookings_history.data.remote.model.RoomBooking
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.browse_rooms.data.remote.model.RoomBookingStatus
import com.example.olpgas.browse_rooms.domain.use_case.RefreshLocalCacheUseCase
import com.example.olpgas.core.util.ConnectivityObserver
import com.example.olpgas.view_room_details.data.local.database.entities.FullRoomDetailsLocal
import com.example.olpgas.view_room_details.domain.use_case.BookRoomUseCase
import com.example.olpgas.view_room_details.domain.use_case.GetFullRoomDetailsFromLocalDBUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RoomDetailsViewModel @Inject constructor(
    private val getFullRoomDetailsFromLocalDBUseCase: GetFullRoomDetailsFromLocalDBUseCase,
    private val bookRoomUseCase: BookRoomUseCase,
    private val refreshLocalCacheUseCase: RefreshLocalCacheUseCase,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {
    lateinit var allRoomDetailsState: LiveData<FullRoomDetailsLocal>

    val connectionStatus = connectivityObserver.observe().asLiveData()
    fun onEvent(event: RoomDetailsEvent) {
        when(event) {
            is RoomDetailsEvent.OnCreate -> allRoomDetailsState = getFullRoomDetailsFromLocalDBUseCase(event.id)
            is RoomDetailsEvent.BookRoom -> bookRoom(event.todayDate, event.noOfPersonStaying)
        }
    }

    private fun bookRoom(bookingDate: String, totalStayingPersons: Int) {
        viewModelScope.launch {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Adjust format if needed

            val calendar = Calendar.getInstance()
            calendar.time = Date()

            // Add 10 days
            calendar.add(Calendar.DAY_OF_YEAR, 10)

            val after10DaysDate = calendar.time
            val paymentDueDate = format.format(after10DaysDate)

            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, 30)
            val after30DaysDate = calendar.time
            val nextPaymentDate = format.format(after30DaysDate)

            val roomBooking = RoomBooking(
                id = null,
                allRoomDetailsState.value?.id!!,
                userId = null,
                allRoomDetailsState.value?.ownerId!!,
                bookingDate,
                paymentDueDate,
                nextPaymentDate,
                totalStayingPersons
            )
            val roomBookingStatus = if((totalStayingPersons + allRoomDetailsState.value?.occupiedBy!!) == allRoomDetailsState.value?.shareable!!) {
                RoomBookingStatus.CompletelyBooked
            } else {
                RoomBookingStatus.PartiallyBooked
            }
            bookRoomUseCase(roomBooking, roomBookingStatus, totalStayingPersons + allRoomDetailsState.value?.occupiedBy!!)
            refreshLocalCacheUseCase()
        }
    }
}