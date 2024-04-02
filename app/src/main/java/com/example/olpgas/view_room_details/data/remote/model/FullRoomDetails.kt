package com.example.olpgas.view_room_details.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class FullRoomDetails(
    val id: Int,
    val roomName: String,
    val ownerId: String,
    val roomNumber: String,
    val streetNumber: String,
    val landMark: String,
    val city: String,
    val state: String,
    val roomArea: Int,
    val shareable: Int,
    val roomType: String,
    val features: List<String>,
    val suitableFor: List<String>,
    val deposit: Int,
    val rentAmount: Int,
    val description: String,
    val roomFeatureId: Int,
    val bookingStatus: String,
    val ratings: Float
)
