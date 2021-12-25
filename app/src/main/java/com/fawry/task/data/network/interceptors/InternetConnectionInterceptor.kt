package com.fawry.task.data.network.interceptors

import com.fawry.task.data.managers.NetworkConnectionManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class InternetConnectionInterceptor @Inject constructor(
    private val networkConnectionManager: NetworkConnectionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        if (!networkConnectionManager.isConnectedToNetwork())
            throw NoInternetException()

        return chain.proceed(chain.request())

    }

    class NoInternetException : IOException()

}