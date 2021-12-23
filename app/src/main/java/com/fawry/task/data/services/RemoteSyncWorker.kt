package com.fawry.task.data.services

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.fawry.task.data.repositories.movies_repository.IMoviesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.*

@HiltWorker
class RemoteSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: IMoviesRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        Log.e("background work", "fetch from remote server @ ${Date()}")

        return try {
            repository.syncMoviesFromRemoteServer()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }

    }

}