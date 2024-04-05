package com.example.olpgas.bookings_history.presentation

sealed class BookingEvent {
    data object OnCreate : BookingEvent()
}