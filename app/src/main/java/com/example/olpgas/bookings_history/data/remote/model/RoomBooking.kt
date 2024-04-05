package com.example.olpgas.bookings_history.data.remote.model

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class RoomBooking(
    val id: Int? = null,
    val roomId: Int,
    val userId: String,
    val ownerId: String,
    val bookingDate: String,
    val paymentDueDate: String,
    val nextPaymentDate: String,
    val totalStayingPersons: Int
)