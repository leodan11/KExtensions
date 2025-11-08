package com.github.leodan11.k_extensions.base

import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

/**
 * Represents the result of a flash operation, which can either be a success or an error.
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
 * val user = User("Alice", 30)
 *
 * val result1 = FlashResult.success("Completed")
 * val result2 = FlashResult.successData(user, "User loaded")
 * val result3 = FlashResult.error("Failed")
 * val result4 = FlashResult.errorData(user, "Failed with user")
 *
 * // Functional handling
 * result2
 *     .onSuccess { user -> println("User loaded: $user") }
 *     .onError { println("Something went wrong") }
 * ```
 *
 * @since 2.2.1
 */
@KSerializable
sealed class FlashResult : Serializable {

    /** Flash operation failure without associated data. */
    @KSerializable
    data class Error(
        val message: String,
        val isSimple: Boolean = true,
        val colorToast: Boolean = true,
        val title: String? = null
    ) : FlashResult()

    /** Flash operation failure with associated data. */
    @KSerializable
    data class ErrorData<T : Any>(
        val data: T,
        val message: String,
        val isSimple: Boolean = true,
        val colorToast: Boolean = true,
        val title: String? = null
    ) : FlashResult()

    /** Flash operation success without associated data. */
    @KSerializable
    data class Success(
        val message: String,
        val isSimple: Boolean = true,
        val showToast: Boolean = true,
        val colorToast: Boolean = true,
        val title: String? = null
    ) : FlashResult()

    /** Flash operation success with associated data. */
    @KSerializable
    data class SuccessData<T : Any>(
        val data: T,
        val message: String,
        val isSimple: Boolean = true,
        val showToast: Boolean = true,
        val colorToast: Boolean = true,
        val title: String? = null
    ) : FlashResult()

    companion object {
        /** Helper for creating a simple error result. */
        fun error(
            message: String,
            isSimple: Boolean = true,
            colorToast: Boolean = true,
            title: String? = null
        ): FlashResult = Error(message, isSimple, colorToast, title)

        /** Helper for creating an error result with data. */
        fun <T : Any> errorData(
            data: T,
            message: String,
            isSimple: Boolean = true,
            colorToast: Boolean = true,
            title: String? = null
        ): FlashResult = ErrorData(data, message, isSimple, colorToast, title)

        /** Helper for creating a simple success result. */
        fun success(
            message: String,
            isSimple: Boolean = true,
            showToast: Boolean = true,
            colorToast: Boolean = true,
            title: String? = null
        ): FlashResult = Success(message, isSimple, showToast, colorToast, title)

        /** Helper for creating a success result with data. */
        fun <T : Any> successData(
            data: T,
            message: String,
            isSimple: Boolean = true,
            showToast: Boolean = true,
            colorToast: Boolean = true,
            title: String? = null
        ): FlashResult = SuccessData(data, message, isSimple, showToast, colorToast, title)
    }
}