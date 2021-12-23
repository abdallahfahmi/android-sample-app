package com.fawry.task

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.fawry.task.data.services.RemoteSyncWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        scheduleDatabaseSyncWorker()
    }

    private fun scheduleDatabaseSyncWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val task = PeriodicWorkRequestBuilder<RemoteSyncWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager
            .getInstance(this)
            .enqueueUniquePeriodicWork(
                "sync-movies",
                ExistingPeriodicWorkPolicy.REPLACE, //todo replace with KEEP
                task
            )
    }

}