package com.example.olpgas.browse_rooms.domain.use_case

import android.app.Application
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.olpgas.browse_rooms.domain.workers.RefreshLocalCacheWorker
import java.time.Duration

class RefreshLocalCacheUseCase(
    private val application: Application
) {
    operator fun invoke(){
        val refreshCacheRequest =
            OneTimeWorkRequestBuilder<RefreshLocalCacheWorker>()
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, Duration.ofSeconds(10))
                .setConstraints(
                    Constraints(
                        requiredNetworkType = NetworkType.CONNECTED,
                        requiresStorageNotLow = true
                    )
                )
                .build()

        WorkManager.getInstance(application).enqueueUniqueWork(
            "RefreshLocalCache",
            ExistingWorkPolicy.KEEP,
            refreshCacheRequest
        )
    }
}