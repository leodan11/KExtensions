@file:Suppress("DEPRECATION")

package com.github.leodan11.k_extensions.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import java.net.HttpURLConnection
import java.net.URL

/**
 * Checks whether a given [serverUrl] is reachable by performing an HTTP connection.
 *
 * **Important:** This function **must not be called on the main thread**, as it performs
 * network I/O. Always call it from a background thread, coroutine, or using `Dispatchers.IO`.
 *
 * The function first checks if the device is online (via [isOnline]) before attempting
 * the HTTP connection. It returns `true` if the server responds with HTTP 200, and `false`
 * otherwise or if any exception occurs.
 *
 * ### Parameters:
 * @receiver [Context] The Android context used to check network connectivity.
 * @param serverUrl The URL of the server to check. Must be a valid HTTP(S) URL.
 * @param timeOut Connection timeout in milliseconds. Default is 10,000 ms (10 seconds).
 *
 * @return `true` if the server is reachable and responds with HTTP 200, `false` otherwise.
 *
 * ### Example usage:
 * ```kotlin
 * lifecycleScope.launch(Dispatchers.IO) {
 *     val reachable = context.isURLReachable("https://www.google.com")
 *     println("Server reachable: $reachable")
 * }
 * ```
 *
 * @see URL
 * @see HttpURLConnection
 * @since 2.2.8
 */
fun Context.isURLReachable(serverUrl: String, timeOut: Int = 10 * 1000): Boolean {
    if (isOnline) {
        return try {
            with(URL(serverUrl).openConnection() as HttpURLConnection) {
                connectTimeout = timeOut
                connect()
                responseCode == 200
            }
        } catch (_: Throwable) {
            false
        }
    }
    return false
}


/**
 * Checks whether the device currently has an active internet connection.
 *
 * This property evaluates network connectivity using [ConnectivityManager]. It supports
 * both pre-API 23 and newer Android versions:
 *
 * - **API < 23:** Uses [NetworkInfo] to check if the device is connected via Wi-Fi, Mobile,
 *   VPN, or Ethernet.
 * - **API ≥ 23:** Uses [NetworkCapabilities] of the active network to verify if one of the
 *   following transports is available: Cellular, Wi-Fi, Ethernet, or VPN.
 *
 * ### Usage:
 * ```kotlin
 * if (context.isOnline) {
 *     println("Device is online")
 * } else {
 *     println("Device is offline")
 * }
 * ```
 *
 * ### Notes:
 * - Does **not guarantee that the internet is actually reachable**; it only checks if the
 *   device has an active network connection capable of internet access.
 * - Recommended to combine with a real connectivity check (e.g., `isURLReachable`) if
 *   you need to confirm server availability.
 *
 * @receiver [Context] The Android context used to access system connectivity services.
 * @return `true` if the device has an active network connection (Wi-Fi, Cellular, VPN, or Ethernet), `false` otherwise.
 *
 * @see ConnectivityManager
 * @see NetworkCapabilities
 * @see Context.isURLReachable
 * @since 2.2.8
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
