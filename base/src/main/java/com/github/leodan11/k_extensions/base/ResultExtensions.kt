@file:Suppress("UNCHECKED_CAST")

package com.github.leodan11.k_extensions.base


/**
 * Executes the given [block] if this [FlashResult] represents a successful result
 * ([FlashResult.Success] or [FlashResult.SuccessData]).
 *
 * The original [FlashResult] is returned for fluent chaining.
 *
 * @param block Action to execute when the result indicates success.
 * @return The same [FlashResult] instance.
 *
 * @since 2.2.2
 */
inline fun FlashResult.onSuccess(block: (FlashResult) -> Unit): FlashResult {
    when (this) {
        is FlashResult.Success, is FlashResult.SuccessData<*> -> block(this)
        else -> Unit
    }
    return this
}

/**
 * Executes the given [block] if this [FlashResult] is a [FlashResult.SuccessData],
 * passing the contained data [T] to it.
 *
 * The original [FlashResult] is returned for fluent chaining.
 *
 * @param T Type of the success data.
 * @param block Action to perform with the success data.
 * @return The same [FlashResult] instance.
 *
 * @since 2.2.2
 */
inline fun <T : Any> FlashResult.onSuccessData(block: (T) -> Unit): FlashResult {
    if (this is FlashResult.SuccessData<*>) {
        @Suppress("UNCHECKED_CAST")
        block(this.data as T)
    }
    return this
}

/**
 * Executes the given [block] if this [FlashResult] represents an error
 * ([FlashResult.Error] or [FlashResult.ErrorData]).
 *
 * The original [FlashResult] is returned for fluent chaining.
 *
 * @param block Action to execute when the result indicates an error.
 * @return The same [FlashResult] instance.
 *
 * @since 2.2.2
 */
inline fun FlashResult.onError(block: (FlashResult) -> Unit): FlashResult {
    when (this) {
        is FlashResult.Error, is FlashResult.ErrorData<*> -> block(this)
        else -> Unit
    }
    return this
}

/**
 * Executes the given [block] if this [FlashResult] is an [FlashResult.ErrorData],
 * passing the contained error data [T] to it.
 *
 * The original [FlashResult] is returned for fluent chaining.
 *
 * @param T Type of the error data.
 * @param block Action to perform with the error data.
 * @return The same [FlashResult] instance.
 *
 * @since 2.2.2
 */
inline fun <T : Any> FlashResult.onErrorData(block: (T) -> Unit): FlashResult {
    if (this is FlashResult.ErrorData<*>) {
        @Suppress("UNCHECKED_CAST")
        block(this.data as T)
    }
    return this
}

/**
 * Transforms the internal data of a [FlashResult.SuccessData] or [FlashResult.ErrorData]
 * using the provided [transform] function.
 *
 * If the result has no data, the same [FlashResult] instance is returned unchanged.
 *
 * @param T Input data type.
 * @param R Output data type after transformation.
 * @param transform Function that maps the original data to a new value.
 * @return A new [FlashResult] with the transformed data, or the original if none exists.
 *
 * @since 2.2.2
 */
inline fun <T : Any, R : Any> FlashResult.mapData(transform: (T) -> R): FlashResult = when (this) {
    is FlashResult.SuccessData<*> -> {
        @Suppress("UNCHECKED_CAST")
        FlashResult.SuccessData(
            data = transform(this.data as T),
            message = this.message,
            isSimple = this.isSimple,
            showToast = this.showToast,
            colorToast = this.colorToast,
            title = this.title
        )
    }
    is FlashResult.ErrorData<*> -> {
        @Suppress("UNCHECKED_CAST")
        FlashResult.ErrorData(
            data = transform(this.data as T),
            message = this.message,
            isSimple = this.isSimple,
            colorToast = this.colorToast,
            title = this.title
        )
    }
    else -> this
}

/**
 * Applies the given [transform] function to the internal data if this is a
 * [FlashResult.SuccessData] or [FlashResult.ErrorData], and flattens the result.
 *
 * Useful for chaining operations that return another [FlashResult].
 *
 * @param T Input data type.
 * @param R Output [FlashResult] type after transformation.
 * @param transform Function that maps the data into a new [FlashResult].
 * @return The transformed [FlashResult], or the same instance if no data exists.
 *
 * @since 2.2.2
 */
inline fun <T : Any, R : Any> FlashResult.flatMap(transform: (T) -> FlashResult): FlashResult = when (this) {
    is FlashResult.SuccessData<*> -> @Suppress("UNCHECKED_CAST") transform(this.data as T)
    is FlashResult.ErrorData<*> -> @Suppress("UNCHECKED_CAST") transform(this.data as T)
    else -> this
}

/**
 * Returns the contained data if this is a [FlashResult.SuccessData] or [FlashResult.ErrorData],
 * or returns the result of [default] otherwise.
 *
 * @param T Expected data type.
 * @param default Value to return if no data exists.
 * @return The contained data or [default] value.
 *
 * @since 2.2.2
 */
inline fun <T : Any> FlashResult.getOrElse(default: () -> T): T = when (this) {
    is FlashResult.SuccessData<*> -> @Suppress("UNCHECKED_CAST") this.data as T
    is FlashResult.ErrorData<*> -> @Suppress("UNCHECKED_CAST") this.data as T
    else -> default()
}

/**
 * Folds this [FlashResult] into a single value by applying either [onSuccess] or [onError]
 * depending on the result type.
 *
 * @param R Return type of the fold operation.
 * @param onSuccess Function applied when result is success.
 * @param onError Function applied when result is error.
 * @return Result of the applied function.
 *
 * @since 2.2.2
 */
inline fun <R> FlashResult.fold(onSuccess: (FlashResult) -> R, onError: (FlashResult) -> R): R =
    when (this) {
        is FlashResult.Success, is FlashResult.SuccessData<*> -> onSuccess(this)
        is FlashResult.Error, is FlashResult.ErrorData<*> -> onError(this)
    }

/**
 * Similar to [fold], but provides access to the internal data [T] (if any).
 *
 * @param T Data type.
 * @param R Return type of the fold operation.
 * @param onSuccess Function applied when result is success, with optional data.
 * @param onError Function applied when result is error, with optional data.
 * @return Result of the applied function.
 *
 * @since 2.2.2
 */
inline fun <T : Any, R> FlashResult.foldData(
    onSuccess: (T?) -> R,
    onError: (T?) -> R
): R = when (this) {
    is FlashResult.SuccessData<*> -> @Suppress("UNCHECKED_CAST") onSuccess(this.data as T)
    is FlashResult.ErrorData<*> -> @Suppress("UNCHECKED_CAST") onError(this.data as T)
    is FlashResult.Success -> onSuccess(null)
    is FlashResult.Error -> onError(null)
}

/**
 * @return `true` if this [FlashResult] is a [FlashResult.Success] or [FlashResult.SuccessData].
 *
 * @since 2.2.2
 */
fun FlashResult.isSuccess(): Boolean =
    this is FlashResult.Success || this is FlashResult.SuccessData<*>

/**
 * @return `true` if this [FlashResult] is a [FlashResult.Error] or [FlashResult.ErrorData].
 *
 * @since 2.2.2
 */
fun FlashResult.isError(): Boolean =
    this is FlashResult.Error || this is FlashResult.ErrorData<*>




/**
 * Executes [block] if this [StatusResult] represents a successful outcome
 * ([StatusResult.Success] or [StatusResult.SuccessData]).
 *
 * Returns the same [StatusResult] instance for chaining.
 *
 * @param block Action to execute when the result is successful.
 * @return The same [StatusResult] instance.
 *
 * @since 2.2.2
 */
inline fun StatusResult.onSuccess(block: (StatusResult) -> Unit): StatusResult {
    if (this is StatusResult.Success || this is StatusResult.SuccessData<*>) block(this)
    return this
}

/**
 * Executes [block] if this [StatusResult] is [StatusResult.SuccessData],
 * passing the contained data [T].
 *
 * Returns the same [StatusResult] instance for chaining.
 *
 * @param T Type of the success data.
 * @param block Action to execute with the data.
 * @return The same [StatusResult] instance.
 *
 * @since 2.2.2
 */
inline fun <T : Any> StatusResult.onSuccessData(block: (T) -> Unit): StatusResult {
    if (this is StatusResult.SuccessData<*>) @Suppress("UNCHECKED_CAST") block(this.data as T)
    return this
}

/**
 * Executes [block] if this [StatusResult] represents an error
 * ([StatusResult.Error] or [StatusResult.ErrorData]).
 *
 * Returns the same [StatusResult] instance for chaining.
 *
 * @param block Action to execute when the result indicates an error.
 * @return The same [StatusResult] instance.
 *
 * @since 2.2.2
 */
inline fun StatusResult.onError(block: (StatusResult) -> Unit): StatusResult {
    if (this is StatusResult.Error || this is StatusResult.ErrorData<*>) block(this)
    return this
}

/**
 * Executes [block] if this [StatusResult] is [StatusResult.ErrorData],
 * passing the contained error data [T].
 *
 * Returns the same [StatusResult] instance for chaining.
 *
 * @param T Type of the error data.
 * @param block Action to execute with the data.
 * @return The same [StatusResult] instance.
 *
 * @since 2.2.2
 */
inline fun <T : Any> StatusResult.onErrorData(block: (T) -> Unit): StatusResult {
    if (this is StatusResult.ErrorData<*>) @Suppress("UNCHECKED_CAST") block(this.data as T)
    return this
}

/**
 * Maps the internal data of a [StatusResult.SuccessData] or [StatusResult.ErrorData]
 * using the provided [transform] function.
 *
 * Returns a new [StatusResult] with transformed data or the same instance if no data exists.
 *
 * @param T Input data type.
 * @param R Output data type after transformation.
 * @param transform Function that maps the existing data to a new value.
 * @return A new [StatusResult] with transformed data, or the same instance if not applicable.
 *
 * @since 2.2.2
 */
inline fun <T : Any, R : Any> StatusResult.mapData(transform: (T) -> R): StatusResult = when (this) {
    is StatusResult.SuccessData<*> -> @Suppress("UNCHECKED_CAST")
    StatusResult.SuccessData(
        data = transform(this.data as T),
        message = message,
        details = details
    )
    is StatusResult.ErrorData<*> -> @Suppress("UNCHECKED_CAST")
    StatusResult.ErrorData(
        data = transform(this.data as T),
        message = message,
        details = details,
        throwable = throwable
    )
    else -> this
}

/**
 * Applies [transform] to the internal data (if any) and flattens the result into a new [StatusResult].
 *
 * Useful for chaining operations that return another [StatusResult].
 *
 * @param T Input data type.
 * @param R Output [StatusResult] type.
 * @param transform Function that transforms data into another [StatusResult].
 * @return The transformed [StatusResult], or the same instance if no data exists.
 *
 * @since 2.2.2
 */
inline fun <T : Any, R : Any> StatusResult.flatMap(transform: (T) -> StatusResult): StatusResult = when (this) {
    is StatusResult.SuccessData<*> -> @Suppress("UNCHECKED_CAST") transform(this.data as T)
    is StatusResult.ErrorData<*> -> @Suppress("UNCHECKED_CAST") transform(this.data as T)
    else -> this
}

/**
 * Returns the contained data if available ([StatusResult.SuccessData] or [StatusResult.ErrorData]),
 * otherwise returns the result of [default].
 *
 * @param T Expected data type.
 * @param default Value to return if no data exists.
 * @return The data or the result of [default].
 *
 * @since 2.2.2
 */
inline fun <T : Any> StatusResult.getOrElse(default: () -> T): T = when (this) {
    is StatusResult.SuccessData<*> -> @Suppress("UNCHECKED_CAST") this.data as T
    is StatusResult.ErrorData<*> -> @Suppress("UNCHECKED_CAST") this.data as T
    else -> default()
}

/**
 * Folds this [StatusResult] into a single value by applying [onSuccess] or [onError]
 * depending on the result type.
 *
 * @param R Return type of the fold.
 * @param onSuccess Function applied when the result is success.
 * @param onError Function applied when the result is error.
 * @return Result of applying either [onSuccess] or [onError].
 *
 * @since 2.2.2
 */
inline fun <R> StatusResult.fold(
    onSuccess: (StatusResult) -> R,
    onError: (StatusResult) -> R
): R = when (this) {
    is StatusResult.Success, is StatusResult.SuccessData<*> -> onSuccess(this)
    is StatusResult.Error, is StatusResult.ErrorData<*> -> onError(this)
}

/**
 * Returns the contained data if available, or `null` otherwise.
 *
 * @param T Expected data type.
 * @return The data or `null` if not present.
 *
 * @since 2.2.2
 */
fun <T : Any> StatusResult.dataOrNull(): T? = when (this) {
    is StatusResult.SuccessData<*> -> @Suppress("UNCHECKED_CAST") this.data as T
    is StatusResult.ErrorData<*> -> @Suppress("UNCHECKED_CAST") this.data as T
    else -> null
}

/**
 * @return `true` if this [StatusResult] represents success.
 *
 * @since 2.2.2
 */
fun StatusResult.isSuccess(): Boolean =
    this is StatusResult.Success || this is StatusResult.SuccessData<*>

/**
 * @return `true` if this [StatusResult] represents an error.
 *
 * @since 2.2.2
 */
fun StatusResult.isError(): Boolean =
    this is StatusResult.Error || this is StatusResult.ErrorData<*>

/**
 * Transforms the message of this [StatusResult] using [transform].
 *
 * This can modify both success and error messages without affecting data or details.
 *
 * @param transform Function to transform the message string.
 * @return A new [StatusResult] with the transformed message.
 *
 * @since 2.2.2
 */
inline fun StatusResult.map(transform: (String) -> String): StatusResult = when (this) {
    is StatusResult.Success -> StatusResult.Success(transform(message), details)
    is StatusResult.SuccessData<*> -> StatusResult.SuccessData(data, transform(message), details)
    is StatusResult.Error -> StatusResult.Error(transform(message), details, throwable)
    is StatusResult.ErrorData<*> -> StatusResult.ErrorData(data, transform(message), details, throwable)
}

/**
 * Transforms only error messages of this [StatusResult] using [transform].
 *
 * Success results remain unchanged.
 *
 * @param transform Function to transform the error message string.
 * @return A new [StatusResult] with the transformed error message or the same instance.
 *
 * @since 2.2.2
 */
inline fun StatusResult.mapError(transform: (String) -> String): StatusResult = when (this) {
    is StatusResult.Error -> StatusResult.Error(transform(message), details, throwable)
    is StatusResult.ErrorData<*> -> StatusResult.ErrorData(data, transform(message), details, throwable)
    else -> this
}

/**
 * Executes [block] for side effects when the result is successful.
 *
 * The result itself is not modified and is returned for chaining.
 *
 * @param block Side effect to execute.
 * @return The same [StatusResult] instance.
 *
 * @since 2.2.2
 */
inline fun StatusResult.tap(block: (StatusResult) -> Unit): StatusResult {
    if (isSuccess()) block(this)
    return this
}

/**
 * Executes [block] for side effects when the result is an error.
 *
 * The result itself is not modified and is returned for chaining.
 *
 * @param block Side effect to execute.
 * @return The same [StatusResult] instance.
 *
 * @since 2.2.2
 */
inline fun StatusResult.tapError(block: (StatusResult) -> Unit): StatusResult {
    if (isError()) block(this)
    return this
}

/**
 * Folds this [StatusResult] into a single value, exposing internal data [T] (if available).
 *
 * @param T Data type.
 * @param R Return type of the fold.
 * @param onSuccess Function applied when success, receives nullable data.
 * @param onError Function applied when error, receives nullable data.
 * @return Result of applying [onSuccess] or [onError].
 *
 * @since 2.2.2
 */
inline fun <T : Any, R> StatusResult.foldData(
    onSuccess: (T?) -> R,
    onError: (T?) -> R
): R = when (this) {
    is StatusResult.SuccessData<*> -> @Suppress("UNCHECKED_CAST") onSuccess(this.data as T)
    is StatusResult.ErrorData<*> -> @Suppress("UNCHECKED_CAST") onError(this.data as T)
    is StatusResult.Success -> onSuccess(null)
    is StatusResult.Error -> onError(null)
}
