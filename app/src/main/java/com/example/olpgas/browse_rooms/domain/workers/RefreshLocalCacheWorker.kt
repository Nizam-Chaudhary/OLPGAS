package com.example.olpgas.browse_rooms.domain.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.browse_rooms.domain.repository.BrowseRoomsRepository
import com.example.olpgas.manage_room.data.remote.SupabaseManageRoom
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
            val allRoomsDetails = browseRoomsRepository.getRoomsForListing()
            val allFullRoomDetails = viewRoomDetailsRepository.getAllFullRoomDetails()

            if(allRoomsDetails != null && allFullRoomDetails != null) {
                for(i in allRoomsDetails.indices) {
                    val fullRoomImages = viewRoomDetailsRepository.getAllFullRoomDetailsImages(allFullRoomDetails[i].ownerId, allFullRoomDetails[i].id)
                    if(!fullRoomImages.isNullOrEmpty()) {
                        viewRoomDetailsRepository.upsert(
                            FullRoomDetailsLocal(
                                allFullRoomDetails[i].id,
                                allFullRoomDetails[i].roomName,
                                allFullRoomDetails[i].ownerId,
                                allFullRoomDetails[i].roomNumber,
                                allFullRoomDetails[i].streetNumber,
                                allFullRoomDetails[i].landMark,
                                allFullRoomDetails[i].city,
                                allFullRoomDetails[i].state,
                                allFullRoomDetails[i].roomArea,
                                allFullRoomDetails[i].shareable,
                                allFullRoomDetails[i].roomType,
                                allFullRoomDetails[i].features,
                                allFullRoomDetails[i].suitableFor,
                                allFullRoomDetails[i].deposit,
                                allFullRoomDetails[i].rentAmount,
                                allFullRoomDetails[i].description,
                                allFullRoomDetails[i].roomFeatureId,
                                allFullRoomDetails[i].bookingStatus,
                                allFullRoomDetails[i].ratings,
                                fullRoomImages
                            )
                        )
                    }

                    val imageUrl = browseRoomsRepository.getRoomsImageForListing(allRoomsDetails[i].ownerId, allRoomsDetails[i].id)
                    if(imageUrl != null) {
                        browseRoomsRepository.upsert(
                            AllRoomsDetailsLocal(
                                allRoomsDetails[i].id,
                                allRoomsDetails[i].ownerId,
                                allRoomsDetails[i].roomName,
                                allRoomsDetails[i].roomNumber,
                                allRoomsDetails[i].description,
                                allRoomsDetails[i].roomFeatureId,
                                allRoomsDetails[i].rentAmount,
                                allRoomsDetails[i].deposit,
                                allRoomsDetails[i].city,
                                allRoomsDetails[i].state,
                                allRoomsDetails[i].ratings,
                                allRoomsDetails[i].bookingStatus,
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

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}
