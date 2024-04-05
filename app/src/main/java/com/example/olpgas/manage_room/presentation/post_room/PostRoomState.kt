package com.example.olpgas.manage_room.presentation.post_room

sealed class PostRoomState {
    data object Nothing: PostRoomState()
    data object Success: PostRoomState()
    data object IsLoading: PostRoomState()
    data class Error(val message: String) : PostRoomState()
}