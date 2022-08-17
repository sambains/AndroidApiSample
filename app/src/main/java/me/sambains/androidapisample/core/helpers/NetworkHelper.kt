package me.sambains.androidapisample.core.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

open class NetworkHelper {

    @Suppress("DEPRECATION")
    fun hasNetworkConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        if (activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            || activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            || activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            return true
        }

        return false
    }
}