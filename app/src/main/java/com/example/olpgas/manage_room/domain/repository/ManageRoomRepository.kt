package com.example.olpgas.manage_room.domain.repository

import androidx.lifecycle.LiveData
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.manage_room.data.remote.model.RoomDetails
import com.example.olpgas.manage_room.data.remote.model.RoomMaster

interface ManageRoomRepository {
    fun getAllOwnedRooms(ownerId: String) : LiveData<List<AllRoomsDetailsLocal>>

    suspend fun insertRoomDetails(roomDetails: RoomDetails) : Int?

    suspend fun insertRoomMaster(roomMaster: RoomMaster) : Int?

    suspend fun uploadImages(ownerId: String, id: Int, images: List<ByteArray>)

    //Update Room Name

    suspend fun updateRoomName(id: Int, newRoomName: String)

    suspend fun updateRoomNameAllRoomDetails(id: Int, newRoomName: String)

    suspend fun updateRoomNameFullRoomDetails(id: Int, newRoomName: String)

    //Update Address
    suspend fun updateAddress(id: Int, streetNumber: String, landMark: String, state: String, city: String)

    suspend fun updateAddressAllRoomDetails(id: Int, state: String, city: String)

    suspend fun updateAddressFullRoomDetails(id: Int, streetNumber: String, landMark: String, state: String, city: String)

    //Update Rent
    suspend fun updateRent(
        id: Int,
        rent: Int
    )

    suspend fun updateRentAllRoomDetails(
        id: Int,
        rent: Int
    )

    suspend fun updateRentFullRoomDetails(
        id: Int,
        rent: Int
    )

    //Update Deposit
    suspend fun updateDeposit(
        id: Int,
        deposit: Int
    )

    suspend fun updateDepositAllRoomDetails(
        id: Int,
        deposit: Int
    )

    suspend fun updateDepositFullRoomDetails(
        id: Int,
        deposit: Int
    )

    //Update ShareableBy
    suspend fun updateShareableBy(
        id: Int,
        shareable: Int
    )

    suspend fun updateShareableByFullRoomDetails(
        id: Int,
        shareable: Int
    )

    //Update Room Type
    suspend fun updateRoomType(
        id: Int,
        roomType: String
    )

    suspend fun updateRoomTypeFullRoomDetails(
        id: Int,
        roomType: String
    )

    //Update Room Area
    suspend fun updateRoomArea(
        id: Int,
        roomArea: Int
    )

    suspend fun updateRoomAreaFullRoomDetails(
        id: Int,
        roomArea: Int
    )

    //Update Description
    suspend fun updateDescription(
        id: Int,
        description: String
    )

    suspend fun updateDescriptionAllRoomDetails(
        id: Int,
        description: String
    )

    suspend fun updateDescriptionFullRoomDetails(
        id: Int,
        description: String
    )

    //Remove Room
    suspend fun removeRoom(
        id: Int,
        roomFeatureId: Int,
        ownerId: String
    )

    suspend fun removeRoomAllRoomDetails(
        id: Int,
    )

    suspend fun removeRoomFullRoomDetails(
        id: Int
    )

    //Update Amenity
    suspend fun updateAmenity(
        roomFeatureId: Int,
        amenity: List<String>
    )

    suspend fun updateAmenityFullRoomDetails(
        id: Int,
        amenity: List<String>
    )

    //Update SuitableFor
    suspend fun updateSuitableFor(
        roomFeatureId: Int,
        suitableFor: List<String>
    )

    suspend fun updateSuitableForFullRoomDetails(
        id: Int,
        suitableFor: List<String>
    )

    suspend fun removeImage(
        ownerId: String,
        id: Int,
        fileName: String
    )

    suspend fun updateImagesUrl(id: Int, imageUrls: List<String>)
}