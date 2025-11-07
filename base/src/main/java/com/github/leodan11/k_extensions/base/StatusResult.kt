package com.github.leodan11.k_extensions.base

import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

/**
 * Represents the result of an operation, which can be either a success or an error.
 * Optionally allows including an object of type [T] associated with the result.
 *
 * This design preserves the flexibility of the original style while using generics
 * to avoid casting from `Any` and improve type safety.
 *
 * @param T Optional type of the associated object. Defaults to `Nothing` when no object is included.
 *
 * ```kotlin
 * // Simple error without object
 * val errorSimple = StatusResult.Error(message = "Operation failed")
 *
 * // Error with object
 * val user = User("John", 25) // Example object
 * val errorWithData = StatusResult.Error(message = "Error with user", data = user)
 *
 * // Simple success without object
 * val successSimple = StatusResult.Success(message = "Operation completed")
 *
 * // Success with object
 * val successWithData = StatusResult.Success(message = "User loaded", data = user)
 * ```
 * @since 2.2.1
 */
@KSerializable
sealed class StatusResult<out T : Any> : Serializable {

    /**
     * Represents an error in the operation.
     *
     * @property message Main error message.
     * @property details Optional additional information about the error.
     * @property throwable Optional exception associated with the error.
     * @property data Optional object related to the error.
     */
    @KSerializable
    data class Error<T : Any> @JvmOverloads constructor(
        val message: String,
        val details: String? = null,
        val throwable: Throwable? = null,
        val data: T? = null
    ) : StatusResult<T>(), Serializable

    /**
     * Represents a successful operation.
     *
     * @property message Main success message.
     * @property details Optional additional information about the success.
     * @property data Optional object related to the success.
     */
    @KSerializable
    data class Success<T : Any> @JvmOverloads constructor(
        val message: String,
        val details: String? = null,
        val data: T? = null
    ) : StatusResult<T>(), Serializable

}
