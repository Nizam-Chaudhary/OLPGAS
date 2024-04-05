package com.example.olpgas.view_room_details.presentation

sealed class RoomDetailsEvent {
    data class OnCreate(val id: Int) : RoomDetailsEvent()

    data class BookRoom(val noOfPersonStaying: Int, val todayDate: String) : RoomDetailsEvent()
}