package com.example.olpgas.manage_room.domain.use_case.update_room

import android.app.Application
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.example.olpgas.manage_room.domain.worker.RemoveRoomWorker
import java.time.Duration

class RemoveRoomUseCase(
    private val application: Application
) {
    suspend operator fun invoke(
        id: Int, roomFeatureId: Int
    ) {
        val dataBuilder = Data.Builder()
        dataBuilder.putInt("id", id)
        dataBuilder.putInt("roomFeatureId", roomFeatureId)
        val inputData = dataBuilder.build()

        val removeRoomRequest =
            OneTimeWorkRequestBuilder<RemoveRoomWorker>()
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, Duration.ofSeconds(10))
                .setInputData(inputData)
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(
                    Constraints(
                        requiredNetworkType = NetworkType.CONNECTED,
                        requiresStorageNotLow = true
                    )
                )
                .build()

        WorkManager.getInstance(application).enqueue(
            removeRoomRequest
        )
    }
}