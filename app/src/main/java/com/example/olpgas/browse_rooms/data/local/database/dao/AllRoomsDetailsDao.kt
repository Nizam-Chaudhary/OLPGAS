package com.example.olpgas.browse_rooms.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal


@Dao
interface AllRoomsDetailsDao {

    @Upsert
    suspend fun upsert(allRoomsDetails: AllRoomsDetailsLocal)

    @Query("SELECT * FROM AllRoomsDetailsLocal")
    fun getAllRoomDetails() : LiveData<List<AllRoomsDetailsLocal>>

    @Query("SELECT * FROM AllRoomsDetailsLocal WHERE ownerId = :ownerId")
    fun getAllOwnedRooms(ownerId: String) : LiveData<List<AllRoomsDetailsLocal>>
}