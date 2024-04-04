package com.example.olpgas.browse_rooms.domain.use_case

import android.app.Application
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.olpgas.browse_rooms.domain.workers.RefreshLocalCacheWorker
import java.time.Duration
import java.util.concurrent.TimeUnit

class GetLocalCacheUseCase(
    private val application: Application
) {
    operator fun invoke(){
        val refreshCacheRequest =
            PeriodicWorkRequestBuilder<RefreshLocalCacheWorker>(
            1, TimeUnit.HOURS)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, Duration.ofSeconds(10))
                .setConstraints(
                    Constraints(
                        requiredNetworkType = NetworkType.CONNECTED,
                        requiresStorageNotLow = true
                    )
                )
            .build()

        WorkManager.getInstance(application).enqueueUniquePeriodicWork(
            "GetLocalCache",
            ExistingPeriodicWorkPolicy.KEEP,
            refreshCacheRequest
        )
    }
}