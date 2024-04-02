package com.example.olpgas.browse_rooms.domain.use_case

import com.example.olpgas.view_room_details.data.local.database.entities.FullRoomDetailsLocal
import com.example.olpgas.view_room_details.domain.repository.ViewRoomDetailsRepository

class RefreshFullRoomDetailsLocalCacheUseCase(
    private val repository: ViewRoomDetailsRepository
) {
    suspend operator fun invoke() {
        val allFullRoomDetails = repository.getAllFullRoomDetails()

        allFullRoomDetails?.let {
            for(item in it) {
                val fullRoomImages = repository.getAllFullRoomDetailsImages(item.ownerId, item.roomName)
                fullRoomImages?.let {
                    repository.upsert(
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
                            fullRoomImages
                        )
                    )
                }
            }
        }
    }
}