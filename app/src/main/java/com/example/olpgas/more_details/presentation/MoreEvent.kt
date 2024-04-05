package com.example.olpgas.more_details.presentation

sealed class MoreEvent {
    data object SignOut: MoreEvent()
    data object OnCreate: MoreEvent()
}