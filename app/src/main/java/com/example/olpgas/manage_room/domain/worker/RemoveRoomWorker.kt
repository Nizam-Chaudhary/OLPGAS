package com.example.olpgas.manage_room.domain.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.olpgas.manage_room.domain.repository.ManageRoomRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class RemoveRoomWorker @AssistedInject constructor(
    private val manageRoomRepository: ManageRoomRepository,
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            val id = inputData.getInt("id", 0)
            val roomFeatureId = inputData.getInt("roomFeatureId", 0)
            val ownerId = inputData.getString("ownerId")

            if(ownerId != null) manageRoomRepository.removeRoom(id, roomFeatureId, ownerId)
            manageRoomRepository.removeRoomAllRoomDetails(id)
            manageRoomRepository.removeRoomFullRoomDetails(id)

            Result.success()
        }catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}