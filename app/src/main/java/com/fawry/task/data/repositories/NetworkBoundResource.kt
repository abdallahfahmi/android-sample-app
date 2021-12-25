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

        //pass viewModelScope to bound all the calls to the lifecycle of the viewModel
        scope.launch(Dispatchers.IO) {

            val data = loadFromDb()

            if (shouldFetch(data)) { //database is empty, fetch from remote

                result.postValue(RemoteResult.loading())

                try {
                    //sync with server and wait until all data is written in database
                    /**
                     * this approach is better than manually starting the worker and observing its state
                     * to update the movies list because here we can propagate any failure to the user
                     * and guarantee a response unlike worker that can be retried several times
                     * before fetching the movies
                     */
                    syncWithRemote()
                    result.postValue(RemoteResult.success(loadFromDb()))
                } catch (e: Exception) {
                    //pass the error to the view in case network failed during sync
                    result.postValue(RemoteResult.error(e))
                }

            } else { //return cached data
                result.postValue(RemoteResult.success(data))
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