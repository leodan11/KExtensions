package com.github.leodan11.k_extensions.core

import android.content.Intent
import android.os.Build
import android.os.Parcelable
import java.io.Serializable

/**
 * Get a serialized model
 *
 * @param T Model
 * @param key Unique key to obtain the data
 * @return
 */
inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}

/**
 * Parcelable
 *
 * @param T Model
 * @param key Unique key to obtain the data.
 * @return
 */
inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}