package com.github.leodan11.k_extensions.base


/**
 * Checks if this [FlashResult] is a success.
 * @since 2.2.3
 */
val FlashResult<*>.isSuccess: Boolean
    get() = this is FlashResult.Success

/**
 * Checks if this [FlashResult] is an error.
 * @since 2.2.3
 */
val FlashResult<*>.isError: Boolean
    get() = this is FlashResult.Error

/**
 * Transforms the successful value of this [FlashResult] using [transform].
 * Returns the same error instance if this is an [FlashResult.Error].
 *
 * @param transform Lambda to transform the success data.
 * @return A new [FlashResult] with the transformed data or the original error.
 * @since 2.2.3
 */
inline fun <T, R> FlashResult<T>.map(transform: (T) -> R): FlashResult<R> = when (this) {
    is FlashResult.Success -> FlashResult.Success(
        data = transform(data),
        message = message,
        isSimple = isSimple,
        showToast = showToast,
        colorToast = colorToast,
        title = title
    )

    is FlashResult.Error -> this
}

/**
 * Returns the success data if this is [FlashResult.Success], or `null` otherwise.
 * @since 2.2.3
 */
fun <T> FlashResult<T>.getOrNull(): T? = when (this) {
    is FlashResult.Success -> data
    is FlashResult.Error -> null
}

/**
 * Returns the success data if this is [FlashResult.Success], or the result of [default] otherwise.
 *
 * @param default Lambda to provide a default value.
 * @return The success data or the default value.
 * @since 2.2.3
 */
inline fun <T> FlashResult<T>.getOrElse(default: () -> T): T = when (this) {
    is FlashResult.Success -> data
    is FlashResult.Error -> default()
}

/** Executes [action] if this is a success and returns this instance.
 * @since 2.2.3 */
inline fun <T> FlashResult<T>.onSuccess(action: (T) -> Unit): FlashResult<T> = apply {
    if (this is FlashResult.Success) action(data)
}

/** Executes [action] if this is an error and returns this instance.
 * @since 2.2.3 */
inline fun <T> FlashResult<T>.onError(action: (FlashResult.Error) -> Unit): FlashResult<T> = apply {
    if (this is FlashResult.Error) action(this)
}

/** Fold function to handle both success and error in one expression.
 * @since 2.2.3 */
inline fun <T, R> FlashResult<T>.fold(
    onSuccess: (T) -> R,
    onError: (FlashResult.Error) -> R
): R = when (this) {
    is FlashResult.Success -> onSuccess(data)
    is FlashResult.Error -> onError(this)
}


// ------------------------
// STATUS RESULT EXTENSIONS
// ------------------------


/**
 * Checks if this [StatusResult] is a success.
 * @since 2.2.3
 */
val StatusResult<*>.isSuccess: Boolean
    get() = this is StatusResult.Success

/**
 * Checks if this [StatusResult] is an error.
 * @since 2.2.3
 */
val StatusResult<*>.isError: Boolean
    get() = this is StatusResult.Error

/**
 * Transforms the successful value of this [StatusResult] using [transform].
 * Returns the same error instance if this is a [StatusResult.Error].
 *
 * @param transform Lambda to transform the success data.
 * @return A new [StatusResult] with the transformed data or the original error.
 * @since 2.2.3
 */
inline fun <T, R> StatusResult<T>.map(transform: (T) -> R): StatusResult<R> = when (this) {
    is StatusResult.Success -> StatusResult.Success(
        data = transform(data),
        message = message,
        details = details
    )

    is StatusResult.Error -> this
}

/**
 * Returns the success data if this is [StatusResult.Success], or `null` otherwise.
 * @since 2.2.3
 */
fun <T> StatusResult<T>.getOrNull(): T? = when (this) {
    is StatusResult.Success -> data
    is StatusResult.Error -> null
}

/**
 * Returns the success data if this is [StatusResult.Success], or the result of [default] otherwise.
 *
 * @param default Lambda to provide a default value.
 * @return The success data or the default value.
 * @since 2.2.3
 */
inline fun <T> StatusResult<T>.getOrElse(default: () -> T): T = when (this) {
    is StatusResult.Success -> data
    is StatusResult.Error -> default()
}

/** Executes [action] if this is a success and returns this instance.
 * @since 2.2.3 */
inline fun <T> StatusResult<T>.onSuccess(action: (T) -> Unit): StatusResult<T> = apply {
    if (this is StatusResult.Success) action(data)
}

/** Executes [action] if this is an error and returns this instance.
 * @since 2.2.3 */
inline fun <T> StatusResult<T>.onError(action: (StatusResult.Error) -> Unit): StatusResult<T> =
    apply {
        if (this is StatusResult.Error) action(this)
    }

/** Fold function to handle both success and error in one expression.
 * @since 2.2.3 */
inline fun <T, R> StatusResult<T>.fold(
    onSuccess: (T) -> R,
    onError: (StatusResult.Error) -> R
): R = when (this) {
    is StatusResult.Success -> onSuccess(data)
    is StatusResult.Error -> onError(this)
}
