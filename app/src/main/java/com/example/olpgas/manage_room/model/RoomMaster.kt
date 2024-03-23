package com.example.olpgas.manage_room.model

import kotlinx.serialization.Serializable

@Serializable
data class RoomMaster(
    val id: Int? = null,
    val roomName: String,
    val ownerId: String? = null,
    val roomNumber: String,
    val streetNumber: String,
    val landMark: String,
    val city: String,
    val state: String,
    val listDate: String? = null,
    var roomFeatureId: Int? = null
)
