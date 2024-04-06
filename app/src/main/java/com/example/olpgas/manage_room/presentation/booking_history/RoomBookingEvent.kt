package com.example.olpgas.manage_room.presentation.booking_history

sealed class RoomBookingEvent {
    data object OnCreate : RoomBookingEvent()
}