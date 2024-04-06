package com.example.olpgas.browse_rooms.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal


@Dao
interface AllRoomsDetailsDao {

    @Upsert
    suspend fun upsert(allRoomsDetails: AllRoomsDetailsLocal)

    @Query("SELECT * FROM AllRoomsDetailsLocal where not bookingStatus = 'Completely Booked'")
    fun getAllRoomDetails() : LiveData<List<AllRoomsDetailsLocal>>

    @Query("SELECT * FROM AllRoomsDetailsLocal WHERE ownerId = :ownerId")
    fun getAllOwnedRooms(ownerId: String) : LiveData<List<AllRoomsDetailsLocal>>

    @Query("DELETE FROM AllRoomsDetailsLocal where id = :id")
    suspend fun delete(id: Int)

    @Query("Select id from AllRoomsDetailsLocal")
    suspend fun getAllRoomIds() : List<Int>

    @Query("UPDATE AllRoomsDetailsLocal SET roomName = :newRoomName WHERE id = :id")
    suspend fun updateRoomName(id: Int, newRoomName: String)

    @Query("UPDATE AllRoomsDetailsLocal SET " +
            "state = :state, " +
            "city = :city" +
            " WHERE id = :id")
    suspend fun updateAddress(
        id: Int,
        state: String,
        city: String
    )

    @Query("UPDATE AllRoomsDetailsLocal SET rentAmount = :rent WHERE id = :id")
    suspend fun updateRent(
        id: Int,
        rent: Int
    )

    @Query("UPDATE AllRoomsDetailsLocal SET deposit = :deposit WHERE id = :id")
    suspend fun updateDeposit(
        id: Int,
        deposit: Int
    )

    @Query("UPDATE AllRoomsDetailsLocal SET description = :description WHERE id = :id")
    suspend fun updateDescription(
        id: Int,
        description: String
    )

    @Query("DELETE FROM AllRoomsDetailsLocal WHERE id = :id")
    suspend fun removeRoom(id: Int)


}