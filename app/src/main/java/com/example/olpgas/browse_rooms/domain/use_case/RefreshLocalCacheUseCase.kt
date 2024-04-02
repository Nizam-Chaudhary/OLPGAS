package com.example.olpgas.browse_rooms.domain.use_case

import android.util.Log
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.browse_rooms.domain.repository.BrowseRoomsRepository

class RefreshLocalCacheUseCase(
    private val repository: BrowseRoomsRepository
) {
    suspend operator fun invoke() {
        val allRoomsDetails = repository.getRoomsForListing()

        allRoomsDetails?.let {
            for(item in it) {
                val imageUrl = repository.getRoomsImageForListing(item.ownerId, item.roomName)
                if(imageUrl != null) {
                    repository.upsert(
                        AllRoomsDetailsLocal(
                            item.id,
                            item.ownerId,
                            item.roomName,
                            item.roomNumber,
                            item.description,
                            item.roomFeatureId,
                            item.rentAmount,
                            item.deposit,
                            item.city,
                            item.state,
                            item.ratings,
                            item.bookingStatus,
                            item.features,
                            imageUrl
                        )
                    )
                }
            }
        }
    }
}