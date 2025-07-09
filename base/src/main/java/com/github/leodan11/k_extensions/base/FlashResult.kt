package com.github.leodan11.k_extensions.base

import android.widget.Toast
import java.io.Serializable

/**
 * Represents the result of an operation, which can either be a success or an error.
 * This is a sealed class, meaning it can only be subclassed within this file.
 */
sealed class FlashResult : Serializable {

    /**
     * Represents a failed result of an operation.
     *
     * @property message Main error message describing the failure.
     * @property isSimple [Boolean] If `true`, the error will be shown as a simple [Toast], otherwise, it will be displayed as if it were a custom Toast.
     * @property colorToast [Boolean] Extra condition in case your personalized toast requires other changes.
     * @property title Optional title for the flash message. If the personalized toast requires it.
     * @property vObject Optional object associated with the error, useful for debugging or context.
     */
    data class Error(
        val message: String,
        val isSimple: Boolean = true,
        val colorToast: Boolean = true,
        val title: String? = null,
        val vObject: Any? = null
    ) : FlashResult()

    /**
     * Represents a successful result of an operation.
     *
     * @property message Descriptive message indicating success.
     * @property isSimple [Boolean] If `true`, the error will be c as a simple [Toast], otherwise, it will be displayed as if it were a custom Toast.
     * @property showToast [Boolean] If `true` shown a toast, otherwise, do not show a toast.
     * @property colorToast [Boolean] Extra condition in case your personalized toast requires other changes.
     * @property title Optional title for the flash message. If the personalized toast requires it.
     * @property vObject Optional object associated with the result, such as returned data.
     *
     */
    data class Success(
        var message: String,
        val isSimple: Boolean = true,
        val showToast: Boolean = true,
        val colorToast: Boolean = true,
        val title: String? = null,
        val vObject: Any? = null
    ) : FlashResult()

}
