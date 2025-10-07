package com.github.leodan11.k_extensions.base

import java.io.Serializable

/**
 * Represents the result of an operation, which can either be a success or an error.
 *
 * This sealed class provides two possible outcomes:
 * - [Error]: Contains information about a failure.
 * - [Success]: Contains information about a successful operation.
 *
 * Implements [Serializable] for easy serialization if needed.
 */
sealed class StatusResult : Serializable {

    /**
     * Represents an error result.
     *
     * @property message A brief message describing the error.
     * @property details Optional detailed information about the error.
     * @property throwable Optional [Throwable] associated with the error, if any.
     * @property vObject Optional additional object related to the error, can be any type.
     */
    data class Error(val message: String, val details: String? = null, val throwable: Throwable? = null, val vObject: Any? = null) : StatusResult()

    /**
     * Represents a successful result.
     *
     * @property message A message describing the success.
     * @property details Optional additional details about the success.
     * @property vObject Optional additional object related to the success, can be any type.
     */
    data class Success(var message: String, val details: String? = null, val vObject: Any? = null) : StatusResult()

}
