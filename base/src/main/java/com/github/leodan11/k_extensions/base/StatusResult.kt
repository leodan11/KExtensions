package com.github.leodan11.k_extensions.base

import java.io.Serializable

/**
 * Represents the result of an operation, which can be either a success with typed data or an error with optional extra information.
 *
 * This sealed class uses generics to ensure type safety in success results while allowing optional additional information in errors.
 *
 * Example usage:
 * ```kotlin
 * // Simple error without extra payload
 * val errorSimple = StatusResult.Error(message = "Operation failed")
 *
 * // Error with extra payload
 * val user = User("John", 25) // Example object
 * val errorWithPayload = StatusResult.Error(message = "Error with user", payload = user)
 *
 * // Success with typed data
 * val success = StatusResult.Success(data = user, message = "User loaded")
 *
 * // Success with nullable data
 * val nullableSuccess: StatusResult<User?> = StatusResult.Success(data = null, message = "No user found")
 * ```
 *
 * @param T Type of the data associated with a successful result.
 * @since 2.2.3
 */
sealed class StatusResult<out T> : Serializable {

    /**
     * Represents an operation failure.
     *
     * @property message Short description of the error.
     * @property details Optional additional details or context.
     * @property throwable Optional underlying exception that caused the error.
     * @property payload Optional extra information associated with the error.
     */
    data class Error(
        val message: String,
        val details: String? = null,
        val throwable: Throwable? = null,
        val payload: Any? = null
    ) : StatusResult<Nothing>()

    /**
     * Represents a successful operation with typed data.
     *
     * @param T Type of the data returned by the successful operation.
     * @property data The data returned by the successful operation.
     * @property message Short description of the success result.
     * @property details Optional additional details or context.
     */
    data class Success<out T>(
        val data: T,
        val message: String,
        val details: String? = null
    ) : StatusResult<T>()

    companion object {
        /**
         * Create a simple error without extra payload.
         */
        fun error(message: String, details: String? = null, throwable: Throwable? = null): StatusResult<Nothing> =
            Error(message, details, throwable)

        /**
         * Create an error with optional payload.
         */
        fun error(message: String, payload: Any?, details: String? = null, throwable: Throwable? = null): StatusResult<Nothing> =
            Error(message, details, throwable, payload)

        /**
         * Create a success with typed data.
         */
        fun <T> success(data: T, message: String, details: String? = null): StatusResult<T> =
            Success(data, message, details)
    }
}
