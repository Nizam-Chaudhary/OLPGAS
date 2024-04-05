package com.example.olpgas.manage_room.presentation.owned_rooms

sealed class OwnedRoomEvents {
    data object OnCreate: OwnedRoomEvents()
    data class OnRemoveRoomClick(val id: Int, val roomFeatureId: Int): OwnedRoomEvents()
}