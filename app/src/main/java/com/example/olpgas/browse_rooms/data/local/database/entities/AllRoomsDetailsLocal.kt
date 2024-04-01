package com.example.olpgas.browse_rooms.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity
data class AllRoomsDetailsLocal(
    @PrimaryKey
    val id: Int,
    val ownerId: String,
    val roomName: String,
    val roomNumber: String,
    val description: String,
    val features: List<String>,
    val roomFeatureId: Int,
    val rentAmount: Int,
    val deposit: Int,
    val city: String,
    val state: String,
    val ratings: Int,
    val bookingStatus: String,
    val imageUrl: String
)