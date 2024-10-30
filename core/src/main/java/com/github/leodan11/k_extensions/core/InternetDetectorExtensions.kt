@file:Suppress("DEPRECATION")

package com.github.leodan11.k_extensions.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.net.HttpURLConnection
import java.net.URL


/**
 * Sends internet statuses as a simple flow of booleans
 * true if user has an active internet connection
 * false if user hasn't
 *
 * @receiver [Context]
 * @param serverUrl [String] the url to ping for successfully internet connection
 * @param timeOut [Int] timeout for the ping
 * @return Flow<Boolean>
 *
 *
 */
fun Context.internetDetection(
    serverUrl: String = "https://www.google.com/",
    timeOut: Int = 10 * 1000
) = InternetDetector(this, serverUrl, timeOut).state


/**
 * Must not be called on the main thread
 *
 * @receiver [Context]
 * @param serverUrl [String]
 * @param timeOut [Int] default is 10 seconds, timeout is in ms
 * @return [Boolean]
 *
 */
fun Context.isURLReachable(serverUrl: String, timeOut: Int = 10 * 1000): Boolean {
    if (isOnline) {
        return try {
            with(URL(serverUrl).openConnection() as HttpURLConnection) {
                connectTimeout = timeOut
                connect()
                responseCode == 200
            }
        } catch (e: Throwable) {
            false
        }
    }
    return false
}


/**
 * Check if internet is compatible with the device
 *
 * @receiver [Context]
 * @return [Boolean]
 *
 */
fun Context.internetCapabilitiesCallback() = callbackFlow {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            trySend(true)
        }

        override fun onLost(network: Network) {
            trySend(false)
        }
    }
    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()
    connectivityManager?.registerNetworkCallback(networkRequest, callback)

    awaitClose {
        connectivityManager?.unregisterNetworkCallback(callback)
    }
}


/**
 * Check if the device is online
 *
 * @receiver [Context]
 * @return [Boolean]
 *
 */
val Context.isOnline: Boolean
    get() {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        if (cm != null) {
            if (Build.VERSION.SDK_INT < 23) {
                val networkInfo = cm.activeNetworkInfo
                if (networkInfo != null) {
                    return networkInfo.isConnected && (networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo.type == ConnectivityManager.TYPE_MOBILE ||
                            networkInfo.type == ConnectivityManager.TYPE_VPN || networkInfo.type == ConnectivityManager.TYPE_ETHERNET)
                }
            } else {
                val network = cm.activeNetwork

                if (network != null) {
                    val nc = cm.getNetworkCapabilities(network)
                    return if (nc == null) {
                        false
                    } else {
                        nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                                nc.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                    }
                }
            }
        }
        return false
    }
