package com.example.olpgas.browse_rooms.domain.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.browse_rooms.domain.repository.BrowseRoomsRepository
import com.example.olpgas.view_room_details.data.local.database.entities.FullRoomDetailsLocal
import com.example.olpgas.view_room_details.domain.repository.ViewRoomDetailsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class RefreshLocalCacheWorker @AssistedInject constructor(
    private val browseRoomsRepository: BrowseRoomsRepository,
    private val viewRoomDetailsRepository: ViewRoomDetailsRepository,
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(context,params) {
    override suspend fun doWork(): Result {
        return try {
            cacheFullRoomDetails()
            cacheBrowseRoomDetails()

            Result.success()
        }catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private suspend fun cacheBrowseRoomDetails() {
        browseRoomsRepository.deleteAllFromLocal()
        val allRoomsDetails = browseRoomsRepository.getRoomsForListing()

        allRoomsDetails?.let {
            for(item in it) {
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
                            item.features,
                            imageUrl
                        )
                    )
                }
            }
        }
    }

    private suspend fun cacheFullRoomDetails() {
        viewRoomDetailsRepository.deleteAllFromLocal()
        val allFullRoomDetails = viewRoomDetailsRepository.getAllFullRoomDetails()

        allFullRoomDetails?.let {
            for(item in it) {
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
                            fullRoomImages
                        )
                    )
                }
            }
        }
    }
}
