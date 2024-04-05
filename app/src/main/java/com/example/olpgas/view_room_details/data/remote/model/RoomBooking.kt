package com.example.olpgas.view_room_details.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RoomBooking(
    val id: Int? = null,
    val roomId: Int,
    val userId: String? = null,
    val ownerId: String,
    val bookingDate: String,
    val paymentDueDate: String,
    val nextPaymentDate: String,
    val totalStayingPersons: Int,
    val paymentStatus: String? = null
)