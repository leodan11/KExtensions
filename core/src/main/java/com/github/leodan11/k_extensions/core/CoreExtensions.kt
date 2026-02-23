package com.github.leodan11.k_extensions.core

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.TypedValue
import android.view.Menu
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import androidx.annotation.StringRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.graphics.createBitmap
import androidx.core.view.size
import com.github.leodan11.k_extensions.core.content.UnitType
import java.io.ByteArrayInputStream
import java.io.Serializable
import java.nio.charset.Charset
import java.util.zip.GZIPInputStream


/**
 * Casts the receiver object to the specified type [T] unsafely.
 *
 * If the receiver is already of type [T], it is returned directly.
 * Otherwise, an unsafe cast is performed which may throw a [ClassCastException].
 *
 * Use with caution: this function can throw if the cast is invalid.
 *
 * ```kotlin
 * val anyValue: Any = 42
 * val intValue: Int = anyValue.unsafeCast()
 * ```
 *
 * @return The receiver cast to type [T].
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> Any.unsafeCast(): T = if (this is T) this else this as T


/**
 * Safely casts the receiver object to the specified type [T].
 *
 * Returns the receiver cast to [T] if possible, or `null` if the cast is invalid.
 *
 * ```kotlin
 * val anyValue: Any = 42
 * val intValue: Int? = anyValue.safeCast()
 * ```
 *
 * @return The receiver cast to type [T], or `null` if the cast is not valid.
 */
inline fun <reified T> Any.safeCast(): T? = this as? T


/**
 * Retrieves a serializable object from the [Bundle] associated with the given [key].
 *
 * Uses the recommended API for Android Tiramisu (API 33) and above, and falls back to the deprecated
 * method for lower API levels with a safe cast.
 *
 * ```kotlin
 * val user: User? = bundle.serializable<User>("user_key")
 * ```
 *
 * @param key The key used to retrieve the serializable from the [Bundle].
 * @return The serializable object of type [T] associated with [key], or `null` if no such mapping exists.
 *
 * @throws ClassCastException If the value is not of the expected type [T].
 *
 * @see Bundle.getSerializable
 */
inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}


/**
 * Decompresses this [ByteArray], assuming it contains GZIP-compressed data,
 * and converts the resulting bytes into a [String] using the provided [charset].
 *
 * This function never throws exceptions. Any error that occurs during
 * decompression or decoding is captured and returned as a [Result.failure].
 *
 * ### Usage
 * ```
 * val text = byteArray.unzip().getOrNull()
 * ```
 *
 * @receiver ByteArray containing GZIP-compressed data.
 * @param charset The [Charset] used to decode the decompressed bytes into a [String].
 * Defaults to [Charsets.UTF_8].
 *
 * @return A [Result] containing the decompressed [String] if successful,
 * or a [Result.failure] wrapping the encountered exception.
 *
 * @since 3.0.0
 */
fun ByteArray.gunzip(charset: Charset = Charsets.UTF_8): Result<String> =
    runCatching {
        GZIPInputStream(ByteArrayInputStream(this))
            .bufferedReader(charset)
            .use { it.readText() }
    }


/**
 * Decodes this [ByteArray] from Base64 into a [String] using the specified [flags] and [charset].
 *
 * ```kotlin
 * val base64Bytes = "SGVsbG8gV29ybGQ=".toByteArray()
 * val decoded = base64Bytes.toBase64Decode()
 * println(decoded) // Prints: Hello World
 * ```
 *
 * @param flags Optional flags for decoding. Defaults to [Base64.DEFAULT].
 * @param charset The character set used to decode the resulting bytes into a string. Defaults to UTF-8.
 * @return The decoded string result.
 */
fun ByteArray.toBase64Decode(flags: Int = Base64.DEFAULT, charset: Charset = Charsets.UTF_8): String = Base64.decode(this, flags).toString(charset)


/**
 * Encodes this [ByteArray] into a Base64 [String] using the specified [flags].
 *
 * ```kotlin
 * val data = "Hello World".toByteArray()
 * val base64Encoded = data.toBase64Encode()
 * println(base64Encoded) // Prints: "SGVsbG8gV29ybGQ="
 * ```
 *
 * @param flags Optional flags for encoding. Defaults to [Base64.DEFAULT].
 * @return The Base64 encoded string representation of this byte array.
 */
fun ByteArray.toBase64Encode(flags: Int = Base64.DEFAULT): String = Base64.encodeToString(this, flags)


/**
 * Converts this [ByteArray] into a hexadecimal [String] representation.
 *
 * Each byte is converted to a two-digit hexadecimal string and concatenated.
 * An optional [separator] can be used to insert characters between each byte.
 *
 * ```kotlin
 * val bytes = byteArrayOf(0x0F, 0xA0, 0xB1)
 * val hexString = bytes.toHexString(separator = ":")
 * println(hexString) // Prints: "0f:a0:b1"
 * ```
 *
 * @param separator A string to insert between each hex byte. Defaults to an empty string (no separator).
 * @return The hexadecimal string representation of this byte array.
 */
fun ByteArray.toHexString(separator: String = ""): String = joinToString(separator) { "%02x".format(it) }


/**
 * Returns a properly formatted display text from the given [value], using the default
 * fallback string resource [R.string.label_text_unknown] if [value] is null, blank, or empty.
 *
 * Each word in the input is capitalized to improve display consistency.
 *
 * @param value The original string (e.g., a name or label), which may be null or blank.
 * @return A formatted string with each word capitalized, or the default fallback string.
 *
 * ### Example:
 * ```kotlin
 * val rawInput: String? = "   john doe"
 * val displayText = context.getDisplayText(rawInput)
 * // Result: "John Doe"
 *
 * val emptyInput: String? = null
 * val displayText = context.getDisplayText(emptyInput)
 * // Result: "Unknown"
 * ```
 *
 * @see [R.string.label_text_unknown]
 * @since 3.0.0
 */
fun Context.getDisplayText(value: String?): String {
    val def = this.getString(R.string.label_text_unknown)
    return this.getDisplayText(value = value, default = def)
}


/**
 * Returns a properly formatted display text from the given [value], using the provided
 * string resource [default] if [value] is null, blank, or empty.
 *
 * Each word in the input is capitalized to improve display consistency.
 *
 * @param value The original string (e.g., a name or label), which may be null or blank.
 * @param default The string resource to use as fallback if [value] is null or blank.
 * @return A formatted string with each word capitalized, or the provided fallback string.
 *
 * ### Example:
 * ```kotlin
 * val rawInput: String? = "   jane smith"
 * val displayText = context.getDisplayText(rawInput, R.string.label_text_unknown)
 * // Result: "Jane Smith"
 * ```
 *
 * @see [R.string.label_text_unknown]
 * @since 2.2.1
 */
fun Context.getDisplayText(value: String?, @StringRes default: Int): String {
    val def = this.getString(default)
    return this.getDisplayText(value = value, default = def)
}


/**
 * Returns a properly formatted display text from the given [value], using the provided
 * [default] string if [value] is null, blank, or empty. If [default] is empty, the
 * fallback string resource [R.string.label_text_unknown] will be used.
 *
 * Each word in the input is capitalized to improve display consistency.
 *
 * @param value The original string (e.g., a name or label), which may be null or blank.
 * @param default The fallback string to use if [value] is null, blank, or empty.
 * @return A formatted string with each word capitalized, or the fallback string.
 *
 * ### Example:
 * ```kotlin
 * val rawInput: String? = "   bob jones"
 * val displayText = context.getDisplayText(rawInput, "Unknown")
 * // Result: "Bob Jones"
 * ```
 *
 * @see [R.string.label_text_unknown]
 * @since 2.2.1
 */
fun Context.getDisplayText(value: String?, default: String): String {
    val def: String = default.ifEmpty { this.getString(R.string.label_text_unknown) }
    return value?.trim()?.takeIf { it.isNotEmpty() }?.split("\\s+".toRegex())?.joinToString(" ") { it.lowercase().replaceFirstChar(Char::titlecase) } ?: def
}


/**
 * Returns a localized string representing elapsed time in selectable units.
 *
 * You can choose which units to show (days, hours, minutes, seconds).
 * Units with zero value will be omitted unless explicitly included.
 *
 * Example:
 * ```kotlin
 * context.toElapsedTimeString(days = 1, hours = 2, minutes = 3, seconds = 4)
 * // Default: "1 day, 2 hours, 3 minutes and 4 seconds"
 *
 * context.toElapsedTimeString(days = 1, hours = 2, minutes = 3, showUnits = listOf(UnitType.DAYS, UnitType.HOURS))
 * // Only shows days and hours: "1 day and 2 hours"
 * ```
 *
 * @param days Number of elapsed days
 * @param hours Number of elapsed hours
 * @param minutes Number of elapsed minutes
 * @param seconds Number of elapsed seconds
 * @param showUnits List of units to display. Default shows all non-zero units.
 * @return A localized, formatted string describing the elapsed time
 *
 * @since 2.2.2
 */
fun Context.toElapsedTimeString(days: Int = 0, hours: Int = 0, minutes: Int = 0, seconds: Int = 0, showUnits: List<UnitType> = listOf(
    UnitType.DAYS, UnitType.HOURS, UnitType.MINUTES, UnitType.SECONDS)): String {
    fun pluralize(count: Int, singularResId: Int, pluralResId: Int) = if (count == 1) getString(singularResId, count) else getString(pluralResId, count)
    val parts = mutableListOf<String>()
    showUnits.forEach { unit ->
        when (unit) {
            UnitType.DAYS -> if (days > 0) parts.add(pluralize(days, R.string.text_value_elapsed_time_with_day_singular, R.string.text_value_elapsed_time_with_day_plural))
            UnitType.HOURS -> if (hours > 0) parts.add(pluralize(hours, R.string.text_value_elapsed_time_singular, R.string.text_value_elapsed_time_plural))
            UnitType.MINUTES -> if (minutes > 0) parts.add(pluralize(minutes, R.string.text_value_elapsed_time_minutes_singular, R.string.text_value_elapsed_time_minutes_plural))
            UnitType.SECONDS -> if (seconds > 0) parts.add(pluralize(seconds, R.string.text_value_elapsed_time_seconds_singular, R.string.text_value_elapsed_time_seconds_plural))
        }
    }
    return when {
        parts.isEmpty() -> getString(R.string.text_value_a_moment_ago)
        parts.size == 1 -> parts[0]
        else -> {
            val last = parts.removeAt(parts.size - 1)
            parts.joinToString(", ") + " " + getString(R.string.text_value_and) + " " + last
        }
    }
}


/**
 * Safely converts a [Drawable] to a [Bitmap].
 *
 * This handles cases where the drawable has no intrinsic width or height by falling back to
 * a minimum size of 1x1 pixel to prevent crashes.
 *
 * @receiver The [Drawable] to convert.
 * @return A [Bitmap] representation of the drawable.
 *
 * @throws IllegalStateException if the drawable cannot be drawn.
 */
fun Drawable.toBitmapSafe(): Bitmap {
    val bitmap = createBitmap(
        intrinsicWidth.takeIf { it > 0 } ?: 1,
        intrinsicHeight.takeIf { it > 0 } ?: 1
    )
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}


/**
 * Retrieves a serializable extra from the [Intent] associated with the given [key].
 *
 * Uses the recommended API for Android Tiramisu (API 33) and above, and falls back to the deprecated
 * method for lower API levels with a safe cast.
 *
 * ```kotlin
 * val user: User? = intent.serializable<User>("user_key")
 * ```
 *
 * @param key The key used to retrieve the serializable extra from the [Intent].
 * @return The serializable extra of type [T] associated with [key], or `null` if no such mapping exists.
 *
 * @throws ClassCastException If the extra is not of the expected type [T].
 *
 * @see Intent.getSerializableExtra
 */
inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
        key,
        T::class.java
    )

    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}


/**
 * Transforms a list of objects into a list of pairs consisting of the original object and its display name.
 * The display name is extracted using the provided [nameProvider] lambda and processed by [Context.getDisplayText]
 * to ensure a standardized, non-null, and user-friendly format based on the app's resources.
 *
 * This is useful when binding lists to UI components like [AutoCompleteTextView] or [Spinner],
 * where a readable display name is required for selection while retaining access to the original model.
 *
 * @param T The type of the original objects in the list.
 * @param context The [Context] used to resolve string resources when the display name is null or empty.
 * @param nameProvider A lambda function that extracts a string (e.g., name, title, label) from each object of type [T].
 * @return A list of pairs where each pair contains the original object and its processed display name.
 *
 * @see Context.getDisplayText
 */
fun <T> List<T>.toDisplayPairList(context: Context, nameProvider: (T) -> String): List<Pair<T, String>> {
    return this.map { item -> item to context.getDisplayText(nameProvider(item)) }
}


/**
 * Enables icon visibility in a [Menu] if possible.
 *
 * This is useful when using [MenuBuilder], which supports showing icons
 * in menus. The function safely casts the menu and handles any exceptions.
 *
 *
 * ```kotlin
 * override fun onCreateOptionsMenu(menu: Menu): Boolean {
 *     menuInflater.inflate(R.menu.my_menu, menu)
 *     menu.enableIcons()
 *     return true
 * }
 * ```
 * @return `true` if icons were successfully enabled, `false` otherwise.
 * @since 2.2.1
 */
@SuppressLint("RestrictedApi")
fun Menu.enableIcons(): Result<Boolean> = runCatching {
    if (this !is MenuBuilder) return@runCatching false
    this.setOptionalIconsVisible(true)
    true
}


/**
 * Enables icon visibility in the menu and applies horizontal margins to icons for better alignment.
 *
 * This method uses reflection to invoke the internal `setOptionalIconsVisible(true)` method
 * on the menu implementation, which is typically a `MenuBuilder` instance in AndroidX.
 * If successful, it adds horizontal padding around each menu item's icon to improve appearance,
 * especially on devices running Lollipop and above.
 *
 * @receiver The [Menu] instance on which to enable icon visibility and apply margins.
 * @param context The [Context] used to convert density-independent pixels (dp) to pixels.
 * @param marginDp The horizontal margin in dp to apply around icons. Default is 16dp.
 * @return `true` if icon visibility was successfully enabled and margins applied, `false` otherwise.
 *
 * @throws ReflectiveOperationException if the internal method cannot be accessed or invoked.
 * @since 2.2.1
 */
@SuppressLint("RestrictedApi")
fun Menu.enableIconsWithMargin(context: Context, marginDp: Int = 16): Result<Boolean> = runCatching {
    if (this !is MenuBuilder) return@runCatching false
    this.setOptionalIconsVisible(true)
    if (marginDp > 0) {
        val marginPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginDp.toFloat(), context.resources.displayMetrics).toInt()
        for (i in 0 until this.size) {
            val item = this.getItem(i)
            item.icon?.let { icon ->
                item.icon = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    InsetDrawable(icon, marginPx, 0, marginPx, 0)
                } else {
                    object : InsetDrawable(icon, marginPx, 0, marginPx, 0) {
                        override fun getIntrinsicWidth(): Int {
                            return intrinsicHeight + marginPx * 2
                        }
                    }
                }
            }
        }
    }
    true
}


/**
 * Returns the simple class name of the receiver object, or `"Unknown"` if the class name is not available.
 *
 * Useful for logging or tagging where a non-null identifier of the object's class is needed.
 *
 * ```kotlin
 * val tag = someObject.tag()
 * println(tag) // Prints something like "MainActivity" or "Unknown"
 * ```
 *
 * @receiver The object whose class name is to be retrieved.
 * @return The simple name of the object's class, or `"Unknown"` if unavailable.
 */
fun <T : Any> T.tag(): String = this::class.simpleName ?: "Unknown"
