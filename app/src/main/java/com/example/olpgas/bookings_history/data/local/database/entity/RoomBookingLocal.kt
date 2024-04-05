package com.example.olpgas.bookings_history.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
data class RoomBookingLocal(
    @PrimaryKey
    val id: Int? = null,
    val roomId: Int,
    val userId: String? = null,
    val ownerId: String,
    val bookingDate: String,
    val paymentDueDate: String,
    val nextPaymentDate: String,
    val totalStayingPersons: Int,
    val paymentStatus: String
)
