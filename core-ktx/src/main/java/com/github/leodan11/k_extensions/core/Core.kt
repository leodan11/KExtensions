package com.github.leodan11.k_extensions.core

import android.content.Intent
import android.os.Build
import android.os.Bundle
import java.io.Serializable

/**
 * Extension method to check if the current API is below a specific version
 *
 * @param api [Int] - API version
 * @param included [Boolean] - Whether to include the specified version
 * @param block [() -> Unit] - Block of code to execute
 */
fun belowApi(api: Int, included: Boolean = false, block: () -> Unit) {
    if (Build.VERSION.SDK_INT < if (included) api + 1 else api) {
        block()
    }
}

/**
 * Extension method to check if the current API is above a specific version
 *
 * @param api [Int] - API version
 * @param included [Boolean] - Whether to include the specified version
 * @param block [() -> Unit] - Block of code to execute
 */
fun aboveApi(api: Int, included: Boolean = false, block: () -> Unit) {
    if (Build.VERSION.SDK_INT > if (included) api - 1 else api) {
        block()
    }
}

/**
 * Extension method to get a specific type from a Bundle
 *
 * @param key [String] - Key of the Bundle
 * @return [T] - Bundle value
 */
inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

/**
 * Extension method to get a specific type from an Intent
 *
 * @param key [String] - Key of the Intent
 * @return [T] - Intent value
 */
inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}


/**
 * Extension method to get the TAG name for all object
 */
fun <T : Any> T.TAG() = this::class.simpleName


/**
 * Extension method to cast any object to a specific type
 *
 * @return [T] - Casted object
 */
inline fun <reified T> Any.toCast(): T = if (this is T) this else this as T