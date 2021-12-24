package com.fawry.task.data.managers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class NetworkConnectionManager(private val context: Context) {

    fun isConnectedToNetwork(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            return (activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
        } else {
            connectivityManager.run {
                //activeNetworkInfo is deprecated on android 9 (M)
                activeNetworkInfo?.run {
                    return (type == ConnectivityManager.TYPE_WIFI
                        || type == ConnectivityManager.TYPE_MOBILE
                        || type == ConnectivityManager.TYPE_ETHERNET)
                }
            }
        }
        return false
    }

}