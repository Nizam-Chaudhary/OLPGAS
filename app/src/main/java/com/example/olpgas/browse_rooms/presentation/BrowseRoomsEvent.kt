package com.example.olpgas.browse_rooms.presentation

sealed class BrowseRoomsEvent {
    data object OnCreate: BrowseRoomsEvent()
    data object OnRefresh: BrowseRoomsEvent()
}