package com.example.olpgas.roomdetails.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class AllRoomsDetails(
    val id: Int,
    val ownerId: String,
    val roomName: String,
    val roomNumber: String,
    val roomFeatureId: Int,
    val roomType: String,
    val rentAmount: Int,
    val deposit: Int,
    val city: String,
    val state: String,
    val images: MutableList<ByteArray>? = null
)

