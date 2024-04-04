package com.example.olpgas.browse_rooms.data.repository

import androidx.lifecycle.LiveData
import com.example.olpgas.browse_rooms.data.local.database.BrowseRoomDatabase
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.browse_rooms.data.remote.SupabaseListRooms
import com.example.olpgas.browse_rooms.data.remote.model.AllRoomDetails
import com.example.olpgas.browse_rooms.domain.repository.BrowseRoomsRepository

class BrowseRoomsRepositoryImpl(
    private val supabaseListRooms: SupabaseListRooms,
    private val database: BrowseRoomDatabase
) : BrowseRoomsRepository {
    override suspend fun getRoomsForListing(): List<AllRoomDetails>? {
        return supabaseListRooms.getRoomsForListing()
    }

    override suspend fun deleteAllFromLocal() {
        database.getAllRoomDetailsDao().deleteAll()
    }

    override suspend fun getRoomsImageForListing(ownerId: String, id: Int): String? {
        return supabaseListRooms.getRoomsImageForListing(ownerId, id)
    }

    override fun getAllRoomDetailsFromLocalDB() : LiveData<List<AllRoomsDetailsLocal>> {
        return database.getAllRoomDetailsDao().getAllRoomDetails()
    }

    override suspend fun upsert(allRoomsDetails: AllRoomsDetailsLocal) {
        database.getAllRoomDetailsDao().upsert(allRoomsDetails)
    }
}