package com.example.olpgas.browse_rooms.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AllRoomDetails(
    val id: Int,
    val ownerId: String,
    val roomName: String,
    val roomNumber: String,
    val description: String,
    val roomFeatureId: Int,
    val rentAmount: Int,
    val deposit: Int,
    val city: String,
    val state: String,
    val ratings: Float,
    val bookingStatus: String,
)
