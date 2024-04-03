package com.example.olpgas.manage_room.presentation.add_room

sealed class PostRoomState {
    data object Success: PostRoomState()
    data class Error(val message: String) : PostRoomState()
}