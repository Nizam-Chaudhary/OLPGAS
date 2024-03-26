package com.example.olpgas.manage_room.model

sealed class WorkState {
    data object Loading: WorkState()
    data class Success(val message: String) : WorkState()
    data class Error(val message: String) : WorkState()
}