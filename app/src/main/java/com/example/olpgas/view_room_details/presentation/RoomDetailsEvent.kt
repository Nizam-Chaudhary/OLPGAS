package com.example.olpgas.view_room_details.presentation

sealed class RoomDetailsEvent {
    data class OnCreate(val id: Int) : RoomDetailsEvent()
}