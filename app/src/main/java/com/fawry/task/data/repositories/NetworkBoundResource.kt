package com.fawry.task.data.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.fawry.task.data.network.RemoteResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class NetworkBoundResource<ResultType>(scope: CoroutineScope) {

    val result: MutableLiveData<RemoteResult<ResultType>> = MutableLiveData()

    init {

        scope.launch(Dispatchers.IO) {

            val cachedData = loadFromDb()

            if (shouldFetch(cachedData)) { //database is empty, fetch from remote

                result.postValue(RemoteResult.loading())

                try {
                    //sync with server and wait until all data is written in database
                    syncWithRemote()
                    result.postValue(RemoteResult.success(loadFromDb()))
                } catch (e: Exception) {
                    //pass the error to the view in case network failed during sync
                    result.postValue(RemoteResult.error(e))
                }

            } else {
                result.postValue(RemoteResult.success(cachedData))
            }

        }
    }

    @WorkerThread
    abstract suspend fun loadFromDb(): ResultType

    @WorkerThread
    abstract fun shouldFetch(data: ResultType): Boolean

    @WorkerThread
    abstract suspend fun syncWithRemote()

}