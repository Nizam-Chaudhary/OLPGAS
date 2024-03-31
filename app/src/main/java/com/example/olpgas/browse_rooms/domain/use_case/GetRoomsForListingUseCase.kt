package com.example.olpgas.browse_rooms.domain.use_case

import com.example.olpgas.browse_rooms.data.remote.model.AllRoomDetails
import com.example.olpgas.browse_rooms.domain.repository.BrowseRoomsRepository

class GetRoomsForListingUseCase(
    private val repository: BrowseRoomsRepository
) {
    suspend operator fun invoke() : List<AllRoomDetails>? {
        return repository.getRoomsForListing()
    }
}