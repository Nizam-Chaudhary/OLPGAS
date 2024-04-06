package com.example.olpgas.bookings_history.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.olpgas.manage_room.presentation.update_room.UpdateRoomEvent
import kotlinx.serialization.Serializable

@Entity
data class RoomBookingLocal(
    @PrimaryKey
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
    val shareableBy: Int,
    val payerName: String,
    val imageUrl: String
)
