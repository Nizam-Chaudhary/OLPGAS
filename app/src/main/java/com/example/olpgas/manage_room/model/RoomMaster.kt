package com.example.olpgas.manage_room.model

data class RoomMaster(
    val id: Int? = null,
    val roomName: String,
    val ownerId: String? = null,
    val roomNumber: String,
    val streetNumber: String,
    val landmark: String,
    val city: String,
    val state: String,
    val listDate: String? = null,
    val roomFeatureId: Int? = null
)
