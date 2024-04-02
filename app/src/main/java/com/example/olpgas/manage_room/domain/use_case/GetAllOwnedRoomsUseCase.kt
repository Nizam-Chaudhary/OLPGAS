package com.example.olpgas.manage_room.domain.use_case

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.core.util.Constants.USER_ID
import com.example.olpgas.manage_room.domain.repository.ManageRoomRepository

class GetAllOwnedRoomsUseCase(
    private val repository: ManageRoomRepository,
    private val authSharedPreferences: SharedPreferences
) {
    operator fun invoke() : LiveData<List<AllRoomsDetailsLocal>>? {
        val ownerId = authSharedPreferences.getString(USER_ID, "")
        return if(ownerId == null) {
            null
        } else {
            repository.getAllOwnedRooms(ownerId)
        }
    }
}