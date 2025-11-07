package com.github.leodan11.k_extensions.base

import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

/**
 * Represents the result of a flash operation, which can either be a success or an error.
 * Optionally allows including an object of type [T] associated with the result.
 *
 * This design provides flexibility while using generics to avoid casting from `Any`
 * and improve type safety.
 *
 * @param T Optional type of the associated object. Defaults to `Nothing` when no object is included.
 *
 * ```kotlin
 * // Error without object
 * val errorSimple = FlashResult.Error(message = "Simple error")
 *
 * // Error with object
 * val user = User("Alice", 30)
 * val errorWithData = FlashResult.Error(message = "Error with user", data = user)
 *
 * // Success without object
 * val successSimple = FlashResult.Success(message = "Operation succeeded")
 *
 * // Success with object
 * val successWithData = FlashResult.Success(message = "User loaded", data = user)
 * ```
 * @since 2.2.1
 */
@KSerializable
sealed class FlashResult<out T : Any> : Serializable {

    /**
     * Represents an error in the flash operation.
     *
     * @property message Main error message.
     * @property isSimple Flag to indicate if the error is simple (default: true).
     * @property colorToast Flag to indicate if the toast should be colored (default: true).
     * @property title Optional title for the error message.
     * @property data Optional object related to the error.
     */
    @KSerializable
    data class Error<T : Any> @JvmOverloads constructor(
        val message: String,
        val isSimple: Boolean = true,
        val colorToast: Boolean = true,
        val title: String? = null,
        val data: T? = null
    ) : FlashResult<T>(), Serializable

    /**
     * Represents a successful flash operation.
     *
     * @property message Main success message.
     * @property isSimple Flag to indicate if the success is simple (default: true).
     * @property showToast Flag to indicate if a toast should be shown (default: true).
     * @property colorToast Flag to indicate if the toast should be colored (default: true).
     * @property title Optional title for the success message.
     * @property data Optional object related to the success.
     */
    @KSerializable
    data class Success<T : Any> @JvmOverloads constructor(
        val message: String,
        val isSimple: Boolean = true,
        val showToast: Boolean = true,
        val colorToast: Boolean = true,
        val title: String? = null,
        val data: T? = null
    ) : FlashResult<T>(), Serializable

}
