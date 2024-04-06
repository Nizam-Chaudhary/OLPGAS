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

    @Query("DELETE FROM FullRoomDetailsLocal where id = :id")
    suspend fun delete(id: Int)

    @Query("UPDATE FullRoomDetailsLocal SET roomName = :newRoomName WHERE id = :id")
    suspend fun updateRoomName(id: Int, newRoomName: String)

    @Query("UPDATE FullRoomDetailsLocal SET " +
            "streetNumber = :streetNumber, " +
            "landMark = :landMark, " +
            "state = :state, " +
            "city = :city" +
            " WHERE id = :id")
    suspend fun updateAddress(
        id: Int,
        streetNumber: String,
        landMark: String,
        state: String,
        city: String
    )

    @Query("UPDATE FullRoomDetailsLocal SET rentAmount = :rent WHERE id = :id")
    suspend fun updateRent(
        id: Int,
        rent: Int
    )

    @Query("UPDATE FullRoomDetailsLocal SET deposit = :deposit WHERE id = :id")
    suspend fun updateDeposit(
        id: Int,
        deposit: Int
    )

    @Query("UPDATE FullRoomDetailsLocal SET shareable = :shareable WHERE id = :id")
    suspend fun updateShareableBy(
        id: Int,
        shareable: Int
    )

    @Query("UPDATE FullRoomDetailsLocal SET roomType = :roomType WHERE id = :id")
    suspend fun updateRoomType(
        id: Int,
        roomType: String
    )

    @Query("UPDATE FullRoomDetailsLocal SET roomArea = :roomArea WHERE id = :id")
    suspend fun updateRoomArea(
        id: Int,
        roomArea: Int
    )

    @Query("UPDATE FullRoomDetailsLocal SET description = :description WHERE id = :id")
    suspend fun updateDescription(
        id: Int,
        description: String
    )

    @Query("DELETE FROM FullRoomDetailsLocal WHERE id = :id")
    suspend fun removeRoom(id: Int)


    @Query("UPDATE FullRoomDetailsLocal SET features = :amenity WHERE id = :id")
    suspend fun updateAmenity(
        id: Int, amenity: List<String>
    )

    @Query("UPDATE FullRoomDetailsLocal SET suitableFor = :suitableFor WHERE id = :id")
    suspend fun updateSuitableFor(
        id: Int, suitableFor: List<String>
    )

    @Query("UPDATE FullRoomDetailsLocal SET  urls = :imageUrls WHERE id = :id")
    suspend fun updateImagesUrl(id: Int, imageUrls: List<String>)
}