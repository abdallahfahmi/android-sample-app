package com.fawry.task.data.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.fawry.task.data.network.RemoteResult
import kotlinx.coroutines.*

abstract class NetworkBoundResource<ResultType>(scope: CoroutineScope) {

    val result: MutableLiveData<RemoteResult<ResultType>> = MutableLiveData()

    init {

        //pass viewModelScope to bound all the calls to the lifecycle of the viewModel
        scope.launch(Dispatchers.IO) {

            val data = loadFromDb()

            if (shouldFetch(data)) { //database is empty, fetch from remote

                result.postValue(RemoteResult.loading())

                try {
                    //sync with server and wait until all data is written in database
                    syncMoviesWithRemote()
                    result.postValue(RemoteResult.success(loadFromDb()!!))
                } catch (e: Exception) {
                    result.postValue(RemoteResult.error(e))
                }

            } else { //return cached data
                result.postValue(RemoteResult.success(data!!)) /**fix this*/
            }

        }
    }

    @WorkerThread
    abstract suspend fun loadFromDb(): ResultType?

    @WorkerThread
    abstract fun shouldFetch(data: ResultType?): Boolean

    @WorkerThread
    abstract suspend fun syncMoviesWithRemote()

}