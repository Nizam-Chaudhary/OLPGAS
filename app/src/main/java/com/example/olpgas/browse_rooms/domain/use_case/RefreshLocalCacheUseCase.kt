package com.example.olpgas.browse_rooms.domain.use_case

import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.browse_rooms.domain.repository.BrowseRoomsRepository
import com.example.olpgas.manage_room.data.remote.SupabaseManageRoom
import com.example.olpgas.view_room_details.data.local.database.entities.FullRoomDetailsLocal
import com.example.olpgas.view_room_details.domain.repository.ViewRoomDetailsRepository

class RefreshLocalCacheUseCase(
    private val browseRoomsRepository: BrowseRoomsRepository,
    private val viewRoomDetailsRepository: ViewRoomDetailsRepository
) {
    suspend operator fun invoke(){
        val allRoomsDetails = browseRoomsRepository.getRoomsForListing()
        val allFullRoomDetails = viewRoomDetailsRepository.getAllFullRoomDetails()

        try {
            if(allRoomsDetails != null && allFullRoomDetails != null) {
                for(item in allFullRoomDetails) {
                    val fullRoomImages = viewRoomDetailsRepository.getAllFullRoomDetailsImages(item.ownerId, item.id)
                    if(!fullRoomImages.isNullOrEmpty()) {
                        viewRoomDetailsRepository.upsert(
                            FullRoomDetailsLocal(
                                item.id,
                                item.roomName,
                                item.ownerId,
                                item.roomNumber,
                                item.streetNumber,
                                item.landMark,
                                item.city,
                                item.state,
                                item.roomArea,
                                item.shareable,
                                item.roomType,
                                item.features,
                                item.suitableFor,
                                item.deposit,
                                item.rentAmount,
                                item.description,
                                item.roomFeatureId,
                                item.bookingStatus,
                                item.ratings,
                                item.occupiedBy,
                                fullRoomImages
                            )
                        )
                    }
                }

                for(item in allRoomsDetails) {
                    val imageUrl = browseRoomsRepository.getRoomsImageForListing(item.ownerId, item.id)
                    if(imageUrl != null) {
                        browseRoomsRepository.upsert(
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
                                imageUrl
                            )
                        )
                    }
                }
            }

            val localRoomIds = browseRoomsRepository.getAllRoomIdsFromLocal()
            val remoteRoomIds = browseRoomsRepository.getAllRoomIds()

            for(localId in localRoomIds) {
                if (remoteRoomIds != null && !remoteRoomIds.contains(SupabaseManageRoom.Id(localId))) {
                    browseRoomsRepository.delete(localId)
                    viewRoomDetailsRepository.delete(localId)
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }
}