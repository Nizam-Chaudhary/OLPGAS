package com.example.olpgas.bookings_history.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class BookingView(
    val id: Int,
    val bookingDate: String,
    val nextPaymentDate: String,
    val ownerId: String,
    val paymentDueDate: String,
    val paymentStatus: String,
    val roomId: Int,
    val totalStayingPersons: Int,
    val userId: String,
    val roomName: String,
    val city: String,
    val rentAmount: Int,
    val deposit: Int,
    val occupiedBy: Int,
    val shareable: Int,
    val userName: String
)