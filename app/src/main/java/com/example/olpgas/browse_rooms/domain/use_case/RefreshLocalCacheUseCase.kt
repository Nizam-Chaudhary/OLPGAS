package com.example.olpgas.browse_rooms.domain.use_case

import com.example.olpgas.browse_rooms.domain.repository.BrowseRoomsRepository

class RefreshLocalCacheUseCase(
    private val repository: BrowseRoomsRepository
) {
    suspend operator fun invoke() {
        val allRoomsDetails = repository.getRoomsForListing()

        val allOwnerId = mutableListOf<String>()
        val allRoomName = mutableListOf<String>()
        if(allRoomsDetails != null) {
            for(i in allRoomsDetails) {
                allOwnerId.add(i.ownerId)
                allRoomName.add(i.roomName)
            }
        }


        val allRoomsImages = repository.getRoomImageForListing(allOwnerId, allRoomName)
        if(allRoomsDetails != null && allRoomsImages != null) {
            repository.saveAllRoomsToLocalDB(allRoomsDetails, allRoomsImages)
        }
    }
}