package com.example.olpgas.manage_room.model

import kotlinx.serialization.Serializable

@Serializable
data class RoomDetails(
    val id: Int? = null,
    val roomArea: Int,
    val shareable: Int,
    val roomType: String,
    val rentAmount: Int,
    val deposit: Int,
    val description: String,
    val suitableFor: List<String>,
    val features: List<String>,
)
