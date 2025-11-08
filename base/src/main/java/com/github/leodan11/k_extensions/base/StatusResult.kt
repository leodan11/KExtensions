package com.github.leodan11.k_extensions.base

import java.io.Serializable

/**
 * Represents the result of an operation, which can be either a success or an error.
 *
 * This sealed class provides four distinct outcomes:
 * 1. [Success]: operation completed successfully without data.
 * 2. [SuccessData]: operation completed successfully with associated data.
 * 3. [Error]: operation failed without associated data.
 * 4. [ErrorData]: operation failed with associated data.
 *
 * Using generics in [SuccessData] and [ErrorData] avoids unsafe casting and improves type safety.
 *
 * Example usage:
 * ```kotlin
 * // Simple error without data
 * val errorSimple = StatusResult.Error(message = "Operation failed")
 *
 * // Error with data
 * val user = User("John", 25) // Example object
 * val errorWithData = StatusResult.ErrorData(data = user, message = "Error with user")
 *
 * // Simple success without data
 * val successSimple = StatusResult.Success(message = "Operation completed")
 *
 * // Success with data
 * val successWithData = StatusResult.SuccessData(data = user, message = "User loaded")
 * ```
 *
 * @since 2.2.1
 */
sealed class StatusResult : Serializable {

    /**
     * Represents an operation failure without associated data.
     *
     * @property message Short description of the error.
     * @property details Optional additional details or context.
     * @property throwable Optional underlying exception that caused the error.
     */
    data class Error(
        val message: String,
        val details: String? = null,
        val throwable: Throwable? = null
    ) : StatusResult()

    /**
     * Represents an operation failure with associated data.
     *
     * @param T Type of the data associated with the error.
     * @property data The data associated with the error (e.g., cached or partial results).
     * @property message Short description of the error.
     * @property details Optional additional details or context.
     * @property throwable Optional underlying exception that caused the error.
     */
    data class ErrorData<T>(
        val data: T,
        val message: String,
        val details: String? = null,
        val throwable: Throwable? = null
    ) : StatusResult()

    /**
     * Represents a successful operation without associated data.
     *
     * @property message Short description of the success result.
     * @property details Optional additional details or context.
     */
    data class Success(
        val message: String,
        val details: String? = null
    ) : StatusResult()

    /**
     * Represents a successful operation with associated data.
     *
     * @param T Type of the data associated with the success.
     * @property data The data returned by the successful operation.
     * @property message Short description of the success result.
     * @property details Optional additional details or context.
     */
    data class SuccessData<T>(
        val data: T,
        val message: String,
        val details: String? = null
    ) : StatusResult()

    companion object {
        fun error(message: String, details: String? = null, throwable: Throwable? = null): StatusResult =
            Error(message, details, throwable)

        fun <T : Any> errorData(data: T, message: String, details: String? = null, throwable: Throwable? = null): StatusResult =
            ErrorData(data, message, details, throwable)

        fun success(message: String, details: String? = null): StatusResult =
            Success(message, details)

        fun <T : Any> successData(data: T, message: String, details: String? = null): StatusResult =
            SuccessData(data, message, details)
    }

}
