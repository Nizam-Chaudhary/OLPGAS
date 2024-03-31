package com.example.olpgas.browse_rooms.domain.use_case

import com.example.olpgas.browse_rooms.domain.repository.BrowseRoomsRepository

class GetRoomsImageForListingUseCase(
    private val repository: BrowseRoomsRepository
) {
    suspend operator fun invoke(
        ownerIds: List<String>, roomNames: List<String>
    ) : List<String>? {
        return repository.getRoomImageForListing(ownerIds, roomNames)
    }
}