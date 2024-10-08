package com.example.olpgas.manage_room.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RoomMaster(
    val roomName: String,
    val ownerId: String,
    val roomNumber: String,
    val streetNumber: String,
    val landMark: String,
    val city: String,
    val state: String,
    val roomFeatureId: Int,
    val bookingStatus: String
)