package com.example.olpgas.browse_rooms.domain.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.olpgas.bookings_history.data.local.database.entity.RoomBookingLocal
import com.example.olpgas.bookings_history.domain.repository.RoomBookingRepository
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
    private val roomBookingRepository: RoomBookingRepository,
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(context,params) {
    override suspend fun doWork(): Result {
        return try {

            val allRoomsDetails = browseRoomsRepository.getRoomsForListing()
            val allFullRoomDetails = viewRoomDetailsRepository.getAllFullRoomDetails()

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
                                item.phoneNumber,
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

            val allRoomBookings = roomBookingRepository.getAllBookings()
            if(allRoomBookings != null) {
                for (item in allRoomBookings) {
                    val imageUrl =
                        roomBookingRepository.getRoomsImageForListing(item.ownerId, item.roomId)
                    if (imageUrl != null) {
                        roomBookingRepository.upsert(
                            RoomBookingLocal(
                                item.id,
                                item.bookingDate,
                                item.nextPaymentDate,
                                item.ownerId,
                                item.paymentDueDate,
                                item.paymentStatus,
                                item.roomId,
                                item.totalStayingPersons,
                                item.userId,
                                item.roomName,
                                item.city,
                                item.rentAmount,
                                item.deposit,
                                item.occupiedBy,
                                item.shareable,
                                item.userName,
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
