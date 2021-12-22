package com.fawry.task.data.network

data class RemoteResult<out T>(
    val status: Status,
    val data: T? = null,
    val error: ResultError? = null
) {
    companion object {
        fun <T> success(data: T): RemoteResult<T> =
            RemoteResult(status = Status.SUCCESS, data = data, error = null)

        fun <T> error(exception: Exception): RemoteResult<T> =
            RemoteResult(status = Status.ERROR, data = null, error = ErrorHandler.handle(exception))

        fun <T> loading(data: T? = null): RemoteResult<T> =
            RemoteResult(status = Status.LOADING, data = data, error = null)
    }

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    fun isSuccess(): Boolean {
        return status == Status.SUCCESS
    }
}

data class ResultError(val type: ErrorHandler.ErrorType, val message: String? = null)