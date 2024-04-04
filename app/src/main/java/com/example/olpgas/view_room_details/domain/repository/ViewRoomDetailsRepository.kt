package com.example.olpgas.view_room_details.domain.repository

import androidx.lifecycle.LiveData
import com.example.olpgas.view_room_details.data.local.database.entities.FullRoomDetailsLocal
import com.example.olpgas.view_room_details.data.remote.model.FullRoomDetails

interface ViewRoomDetailsRepository {

    suspend fun upsert(fullRoomDetailsLocal: FullRoomDetailsLocal)

    fun getFullRoomDetails(id: Int) : LiveData<FullRoomDetailsLocal>

    suspend fun getAllFullRoomDetails() : List<FullRoomDetails>?

    suspend fun getAllFullRoomDetailsImages(ownerIds: String, id: Int) : List<String>?

    suspend fun deleteAllFromLocal()
}