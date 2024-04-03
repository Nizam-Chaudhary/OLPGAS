package com.example.olpgas.manage_room.presentation.post_room

sealed class PostRoomState {
    data object Success: PostRoomState()
    data class IsLoading(val isLoading: Boolean): PostRoomState()
    data class Error(val message: String) : PostRoomState()
}