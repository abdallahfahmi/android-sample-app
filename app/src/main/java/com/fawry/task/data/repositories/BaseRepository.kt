package com.fawry.task.data.repositories

import com.fawry.task.data.network.RemoteResult

abstract class BaseRepository {

    suspend fun <T> makeSafeApiCall(apiCall: suspend () -> T): RemoteResult<T> {
        return try {
            return RemoteResult.success(apiCall.invoke())
        } catch (e: Exception) {
            RemoteResult.error(e)
        }
    }

}