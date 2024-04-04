package com.example.olpgas.manage_room.data.repository

import androidx.lifecycle.LiveData
import com.example.olpgas.browse_rooms.data.local.database.BrowseRoomDatabase
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.manage_room.data.remote.SupabaseManageRoom
import com.example.olpgas.manage_room.data.remote.model.RoomDetails
import com.example.olpgas.manage_room.data.remote.model.RoomMaster
import com.example.olpgas.manage_room.domain.repository.ManageRoomRepository
import com.example.olpgas.view_room_details.data.local.database.FullRoomDetailsDatabase

class ManageRoomRepositoryImpl(
    private val allRoomsDetailsDatabase: BrowseRoomDatabase,
    private val fullRoomDetailsDatabase: FullRoomDetailsDatabase,
    private val supabaseManageRoom: SupabaseManageRoom
) : ManageRoomRepository{
    override fun getAllOwnedRooms(ownerId: String): LiveData<List<AllRoomsDetailsLocal>> {
        return allRoomsDetailsDatabase.getAllRoomDetailsDao().getAllOwnedRooms(ownerId)
    }

    override suspend fun insertRoomDetails(roomDetails: RoomDetails): Int? {
        return supabaseManageRoom.insertRoomDetails(roomDetails)
    }

    override suspend fun insertRoomMaster(roomMaster: RoomMaster): Int? {
        return supabaseManageRoom.insertRoomMaster(roomMaster)
    }

    override suspend fun uploadImages(ownerId: String, id: Int, images: List<ByteArray>){
        supabaseManageRoom.uploadImages(ownerId, id, images)
    }

    override suspend fun updateRoomName(
        id: Int,
        newRoomName: String
    ) {
        supabaseManageRoom.updateRoomName(id, newRoomName)
    }

    override suspend fun updateRoomNameAllRoomDetails(id: Int, newRoomName: String) {
        allRoomsDetailsDatabase.getAllRoomDetailsDao().updateRoomName(id, newRoomName)
    }

    override suspend fun updateRoomNameFullRoomDetails(id: Int, newRoomName: String) {
        fullRoomDetailsDatabase.getFullRoomDetailsDao().updateRoomName(id, newRoomName)
    }

    override suspend fun updateAddress(
        id: Int,
        streetNumber: String,
        landMark: String,
        state: String,
        city: String
    ) {
        supabaseManageRoom.updateAddress(id, streetNumber, landMark, state, city)
    }

    override suspend fun updateAddressAllRoomDetails(
        id: Int,
        state: String,
        city: String
    ) {
        allRoomsDetailsDatabase.getAllRoomDetailsDao().updateAddress(id, state, city)
    }

    override suspend fun updateAddressFullRoomDetails(
        id: Int,
        streetNumber: String,
        landMark: String,
        state: String,
        city: String
    ) {
        fullRoomDetailsDatabase.getFullRoomDetailsDao().updateAddress(id, streetNumber, landMark, state, city)
    }

    override suspend fun updateRent(id: Int, rent: Int) {
        supabaseManageRoom.updateRent(id, rent)
    }

    override suspend fun updateRentAllRoomDetails(id: Int, rent: Int) {
        allRoomsDetailsDatabase.getAllRoomDetailsDao().updateRent(id, rent)
    }

    override suspend fun updateRentFullRoomDetails(id: Int, rent: Int) {
        fullRoomDetailsDatabase.getFullRoomDetailsDao().updateRent(id, rent)
    }

    override suspend fun updateDeposit(id: Int, deposit: Int) {
        supabaseManageRoom.updateDeposit(id, deposit)
    }

    override suspend fun updateDepositAllRoomDetails(id: Int, deposit: Int) {
        allRoomsDetailsDatabase.getAllRoomDetailsDao().updateDeposit(id, deposit)
    }

    override suspend fun updateDepositFullRoomDetails(id: Int, deposit: Int) {
        fullRoomDetailsDatabase.getFullRoomDetailsDao().updateDeposit(id, deposit)
    }

    override suspend fun updateShareableBy(id: Int, shareable: Int) {
        supabaseManageRoom.updateShareableBy(id, shareable)
    }

    override suspend fun updateShareableByFullRoomDetails(id: Int, shareable: Int) {
        fullRoomDetailsDatabase.getFullRoomDetailsDao().updateShareableBy(id, shareable)
    }

    override suspend fun updateRoomType(id: Int, roomType: String) {
        supabaseManageRoom.updateRoomType(id, roomType)
    }

    override suspend fun updateRoomTypeFullRoomDetails(id: Int, roomType: String) {
        fullRoomDetailsDatabase.getFullRoomDetailsDao().updateRoomType(id, roomType)
    }

    override suspend fun updateRoomArea(id: Int, roomArea: Int) {
        supabaseManageRoom.updateRoomArea(id, roomArea)
    }

    override suspend fun updateRoomAreaFullRoomDetails(id: Int, roomArea: Int) {
        fullRoomDetailsDatabase.getFullRoomDetailsDao().updateRoomArea(id, roomArea)
    }

    override suspend fun updateDescription(id: Int, description: String) {
        supabaseManageRoom.updateDescription(id, description)
    }

    override suspend fun updateDescriptionAllRoomDetails(id: Int, description: String) {
        allRoomsDetailsDatabase.getAllRoomDetailsDao().updateDescription(id, description)
    }

    override suspend fun updateDescriptionFullRoomDetails(id: Int, description: String) {
        fullRoomDetailsDatabase.getFullRoomDetailsDao().updateDescription(id, description)
    }
}