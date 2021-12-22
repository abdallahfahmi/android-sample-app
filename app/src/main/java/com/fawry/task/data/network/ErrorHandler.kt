package com.fawry.task.data.network

import org.json.JSONObject
import retrofit2.HttpException
import java.util.concurrent.TimeoutException

object ErrorHandler {

    fun handle(exception: Exception): ResultError {

        return when (exception) {
            is HttpException -> {
                when (exception.code()) {
                    400 -> {
                        handleCustomError(exception, ErrorType.BAD_REQUEST_ERROR)
                    }
                    401 -> {
                        ResultError(ErrorType.AUTHENTICATION_ERROR)
                    }
                    403 -> {
                        handleCustomError(exception, ErrorType.AUTHORIZATION_ERROR)
                    }
                    404 -> {
                        handleCustomError(exception, ErrorType.NOT_FOUND_ERROR)
                    }
                    else -> {
                        ResultError(ErrorType.SERVER_ERROR)
                    }
                }
            }
            is TimeoutException -> ResultError(ErrorType.TIME_OUT_ERROR)
            else -> ResultError(ErrorType.UNKNOWN_ERROR, exception.message)
        }

    }

    private fun handleCustomError(exception: HttpException, type: ErrorType): ResultError {
        return try {
            val errors = JSONObject(exception.response()?.errorBody()?.string() ?: "")
            //handle custom errors here
            ResultError(type, errors.getJSONObject("meta").getString("message"))
        } catch (e: Exception) {
            ResultError(type)
        }
    }

    enum class ErrorType {
        BAD_REQUEST_ERROR,
        AUTHORIZATION_ERROR,
        AUTHENTICATION_ERROR,
        NOT_FOUND_ERROR,
        SERVER_ERROR,
        TIME_OUT_ERROR,
        NO_INTERNET_CONNECTION,
        UNKNOWN_ERROR
    }

}