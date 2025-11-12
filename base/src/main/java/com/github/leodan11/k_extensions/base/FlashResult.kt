package com.github.leodan11.k_extensions.base

import java.io.Serializable

/**
 * Represents the result of a flash operation, which can either be a success with typed data
 * or an error with optional extra payload.
 *
 * Example usage:
 * ```kotlin
 * val user = User("Alice", 30)
 *
 * val result1 = FlashResult.success("Completed")
 * val result2 = FlashResult.success(user, "User loaded")
 * val result3 = FlashResult.error("Failed")
 * val result4 = FlashResult.error("Failed with user", payload = user)
 *
 * // Functional handling
 * when (result2) {
 *     is FlashResult.Success -> println("Message: ${result2.message}")
 *     is FlashResult.Error -> println("Error: ${result2.message}")
 * }
 * ```
 *
 * @param T Type of data returned by a successful operation.
 * @since 2.2.3
 */
sealed class FlashResult<out T> : Serializable {

    /**
     * Represents a flash operation failure.
     *
     * @property message Short description of the error.
     * @property isSimple Indicates if the error should be shown in a simple way.
     * @property colorToast Optional flag to use colored toast.
     * @property title Optional title for the error.
     * @property payload Optional additional data associated with the error.
     */
    data class Error(
        val message: String,
        val isSimple: Boolean = true,
        val colorToast: Boolean = true,
        val title: String? = null,
        val payload: Any? = null
    ) : FlashResult<Nothing>()

    /**
     * Represents a successful flash operation with typed data.
     *
     * @param T Type of the data returned by the operation.
     * @property data Data returned by the operation.
     * @property message Short description of the success.
     * @property isSimple Flag to show in simple style.
     * @property showToast Flag to display toast.
     * @property colorToast Flag for colored toast.
     * @property title Optional title for the success.
     */
    data class Success<out T>(
        val data: T,
        val message: String,
        val isSimple: Boolean = true,
        val showToast: Boolean = true,
        val colorToast: Boolean = true,
        val title: String? = null
    ) : FlashResult<T>()

    companion object {
        /** Helper for creating a simple error without payload. */
        fun error(
            message: String,
            isSimple: Boolean = true,
            colorToast: Boolean = true,
            title: String? = null
        ): FlashResult<Nothing> = Error(message, isSimple, colorToast, title)

        /** Helper for creating an error with optional payload. */
        fun error(
            message: String,
            payload: Any?,
            isSimple: Boolean = true,
            colorToast: Boolean = true,
            title: String? = null
        ): FlashResult<Nothing> = Error(message, isSimple, colorToast, title, payload)

        /** Helper for creating a success without data (can use Unit as data). */
        fun success(
            message: String,
            isSimple: Boolean = true,
            showToast: Boolean = true,
            colorToast: Boolean = true,
            title: String? = null
        ): FlashResult<Unit> = Success(Unit, message, isSimple, showToast, colorToast, title)

        /** Helper for creating a success with typed data. */
        fun <T> success(
            data: T,
            message: String,
            isSimple: Boolean = true,
            showToast: Boolean = true,
            colorToast: Boolean = true,
            title: String? = null
        ): FlashResult<T> = Success(data, message, isSimple, showToast, colorToast, title)
    }
}