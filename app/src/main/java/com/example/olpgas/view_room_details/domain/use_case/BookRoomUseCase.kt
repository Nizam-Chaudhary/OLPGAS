package com.example.olpgas.view_room_details.domain.use_case

import com.example.olpgas.bookings_history.data.remote.model.RoomBooking
import com.example.olpgas.browse_rooms.data.remote.model.RoomBookingStatus
import com.example.olpgas.view_room_details.domain.repository.ViewRoomDetailsRepository

class BookRoomUseCase(
    private val viewRoomDetailsRepository: ViewRoomDetailsRepository
) {
    suspend operator fun invoke(
        booking: RoomBooking,
        roomBookingStatus: RoomBookingStatus,
        totalOccupiedBy: Int
    ) {
        viewRoomDetailsRepository.bookRoom(booking, roomBookingStatus, totalOccupiedBy)
    }
}