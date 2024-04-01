package com.example.olpgas.browse_rooms.data.local.database.type_converters

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer


class Converters {
    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return if (value.isNullOrEmpty()) {
            emptyList()
        } else {
            try {
                val format = Json.decodeFromString<List<String>>(value)
                format
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    @TypeConverter
    fun toString(list: List<String>?): String? {
        return if (list.isNullOrEmpty()) {
            null
        } else {
            try {
                Json.encodeToString(Json.serializersModule.serializer(), list)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}