package com.example.olpgas.view_room_details.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.olpgas.view_room_details.data.local.database.entities.FullRoomDetailsLocal

@Dao
interface FullRoomDetailsDao {
    @Upsert
    suspend fun upsert(fullRoomDetailsLocal: FullRoomDetailsLocal)

    @Query("SELECT * FROM FullRoomDetailsLocal WHERE id = :id ")
    fun getFullRoomDetails(id: Int) : LiveData<FullRoomDetailsLocal>
}