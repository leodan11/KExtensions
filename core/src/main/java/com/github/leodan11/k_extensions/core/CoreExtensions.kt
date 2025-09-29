package com.github.leodan11.k_extensions.core

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BlendMode
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.graphics.createBitmap
import androidx.core.graphics.get
import androidx.core.graphics.set
import androidx.fragment.app.Fragment
import java.io.Serializable
import java.nio.charset.Charset
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs


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
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
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
 * Creates a new [Bitmap] by overlaying the receiver bitmap on top of the [baseBitmap].
 *
 * The receiver bitmap is drawn at the specified offset position with optional transparency.
 *
 * ```kotlin
 * val baseBitmap: Bitmap = ...
 * val overlayBitmap: Bitmap = ...
 * val mergedBitmap = overlayBitmap.mergeBitmaps(baseBitmap,offsetX = 50,offsetY = 100,alpha = 128)
 * ```
 *
 * @param baseBitmap The bitmap that will serve as the background.
 * @param offsetX Horizontal offset (in pixels) to draw the receiver bitmap. Defaults to centered.
 * @param offsetY Vertical offset (in pixels) to draw the receiver bitmap. Defaults to centered.
 * @param alpha Alpha value (transparency) for the receiver bitmap. Range 0 (transparent) to 255 (opaque). Defaults to 255.
 * @return A new [Bitmap] combining both bitmaps.
 */
fun Bitmap.mergeBitmaps(baseBitmap: Bitmap, offsetX: Float = (baseBitmap.width - this.width) / 2f, offsetY: Float = (baseBitmap.height - this.height) / 2f, alpha: Int = 255): Bitmap {
    val combined = createBitmap(baseBitmap.width, baseBitmap.height, baseBitmap.config ?: Bitmap.Config.ARGB_8888)
    val canvas = Canvas(combined)
    val paint = Paint().apply { this.alpha = alpha.coerceIn(0, 255) }
    canvas.drawBitmap(baseBitmap, Matrix(), null)
    canvas.drawBitmap(this, offsetX, offsetY, paint)
    return combined
}


/**
 * Creates a new [Bitmap] by overlaying the receiver bitmap on top of the [baseBitmap]
 * using the specified [blendMode] to combine the pixels.
 *
 * ```kotlin
 * val baseBitmap: Bitmap = ...
 * val overlayBitmap: Bitmap = ...
 * val mergedBitmap = overlayBitmap.mergeWithBlendMode(
 *     baseBitmap,
 *     blendMode = PorterDuff.Mode.MULTIPLY,
 *     alpha = 180
 * )
 * ```
 *
 * @param baseBitmap The bitmap that will serve as the background.
 * @param blendMode The [PorterDuff.Mode] to use when blending the receiver bitmap on top of the base bitmap.
 *                  Defaults to [PorterDuff.Mode.SRC_OVER] (normal drawing).
 * @param offsetX Horizontal offset (in pixels) to draw the receiver bitmap. Defaults to centered.
 * @param offsetY Vertical offset (in pixels) to draw the receiver bitmap. Defaults to centered.
 * @param alpha Alpha value (transparency) for the receiver bitmap. Range 0 (transparent) to 255 (opaque). Defaults to 255.
 * @return A new [Bitmap] combining both bitmaps with the blend mode applied.
 */
fun Bitmap.mergeWithBlendMode(baseBitmap: Bitmap, blendMode: PorterDuff.Mode = PorterDuff.Mode.SRC_OVER, offsetX: Float = (baseBitmap.width - this.width) / 2f, offsetY: Float = (baseBitmap.height - this.height) / 2f, alpha: Int = 255): Bitmap {
    val combined = createBitmap(baseBitmap.width, baseBitmap.height, baseBitmap.config ?: Bitmap.Config.ARGB_8888)
    val canvas = Canvas(combined)
    val paint = Paint().apply {
        this.alpha = alpha.coerceIn(0, 255)
        this.xfermode = PorterDuffXfermode(blendMode)
        isAntiAlias = true
        isFilterBitmap = true
    }
    // Draw base bitmap first without blend mode
    canvas.drawBitmap(baseBitmap, Matrix(), null)
    // Draw receiver bitmap with blend mode
    canvas.drawBitmap(this, offsetX, offsetY, paint)
    // Clear X-fer_mode to avoid affecting subsequent draw calls
    paint.xfermode = null
    return combined
}


/**
 * Creates a new [Bitmap] by overlaying the receiver bitmap on top of the [baseBitmap]
 * using the specified [blendMode] (API 29+).
 *
 * ```kotlin
 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
 *     val baseBitmap: Bitmap = ...
 *     val overlayBitmap: Bitmap = ...
 *     val mergedBitmap = overlayBitmap.mergeWithModernBlendMode(
 *         baseBitmap,
 *         blendMode = BlendMode.MULTIPLY,
 *         alpha = 180
 *     )
 * }
 * ```
 *
 * @param baseBitmap The bitmap that will serve as the background.
 * @param blendMode The [BlendMode] to use when blending the receiver bitmap on top of the base bitmap.
 *                  Defaults to [BlendMode.SRC_OVER] (normal drawing).
 * @param offsetX Horizontal offset (in pixels) to draw the receiver bitmap. Defaults to centered.
 * @param offsetY Vertical offset (in pixels) to draw the receiver bitmap. Defaults to centered.
 * @param alpha Alpha value (transparency) for the receiver bitmap. Range 0 (transparent) to 255 (opaque). Defaults to 255.
 * @return A new [Bitmap] combining both bitmaps with the blend mode applied.
 *
 */
@RequiresApi(Build.VERSION_CODES.Q)
fun Bitmap.mergeWithModernBlendMode(baseBitmap: Bitmap, blendMode: BlendMode = BlendMode.SRC_OVER, offsetX: Float = (baseBitmap.width - this.width) / 2f, offsetY: Float = (baseBitmap.height - this.height) / 2f, alpha: Int = 255): Bitmap {
    val combined = createBitmap(baseBitmap.width, baseBitmap.height, baseBitmap.config ?: Bitmap.Config.ARGB_8888)
    val canvas = Canvas(combined)
    val paint = Paint().apply {
        this.alpha = alpha.coerceIn(0, 255)
        this.blendMode = blendMode
        isAntiAlias = true
        isFilterBitmap = true
    }
    // Draw base bitmap first without blend mode
    canvas.drawBitmap(baseBitmap, Matrix(), null)
    // Draw receiver bitmap with modern blend mode
    canvas.drawBitmap(this, offsetX, offsetY, paint)
    return combined
}


/**
 * Creates a copy of the bitmap with all pixels matching a specified color (within a given tolerance)
 * made transparent.
 *
 * This is useful for removing solid backgrounds like white or any flat color.
 *
 * @receiver The source [Bitmap] to process.
 * @param colorToRemove The target color to remove from the bitmap.
 * @param tolerance The allowed deviation per color channel when comparing with [colorToRemove].
 *                  Defaults to `10`. Must be in the range 0..255.
 * @return A new [Bitmap] with the background pixels made transparent.
 *
 * @see Color.TRANSPARENT
 */
fun Bitmap.removeBackground(colorToRemove: Int, tolerance: Int = 10): Bitmap {
    val output = copy(Bitmap.Config.ARGB_8888, true)

    for (x in 0 until width) {
        for (y in 0 until height) {
            val pixel = this[x, y]
            if (isSimilarColor(pixel, colorToRemove, tolerance)) {
                output[x, y] = Color.TRANSPARENT
            }
        }
    }
    return output
}

/**
 * Returns a new [Bitmap] tinted with the specified [color].
 *
 * This function applies a color filter over the original bitmap, replacing its colors
 * with the given [color], preserving the bitmap's alpha channel.
 *
 * @receiver The original bitmap to be tinted.
 * @param color The color to apply as a tint. Must be a valid color int (use [ColorInt]).
 * @return A new [Bitmap] tinted with the specified [color].
 *
 * @throws IllegalStateException if the bitmap is recycled.
 */
fun Bitmap.tintWithColor(@ColorInt color: Int): Bitmap {
    check(!isRecycled) { "Cannot tint a recycled Bitmap." }

    val result = createBitmap(width, height)
    val canvas = Canvas(result)
    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
    val paint = Paint().apply {
        isAntiAlias = true
        colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
    }
    canvas.drawBitmap(this, 0f, 0f, paint)
    return result
}


/**
 * Returns a properly formatted display text from the given [value], or a default string resource
 * if the input is `null`, blank, or empty.
 *
 * Each word in the input is capitalized to improve display consistency.
 *
 * @param value The original string (e.g., a name or label), which may be null, blank, or improperly formatted.
 * @param default A string resource to use as fallback when [value] is null or blank.
 * @return A formatted string with each word capitalized, or the fallback string.
 *
 * Example usage:
 * ```
 * val rawInput: String? = "   john doe"
 * val displayText = context.getDisplayText(rawInput)
 * // Result: "John Doe"
 *
 * val emptyInput: String? = null
 * val displayText = context.getDisplayText(emptyInput)
 * // Result: "Unknown"
 * ```
 * @see R.string.label_text_unknown for the fallback string resource.
 */
fun Context.getDisplayText(value: String?, @StringRes default: Int = R.string.label_text_unknown): String {
    return value?.trim()?.takeIf { it.isNotEmpty() }?.split("\\s+".toRegex())?.joinToString(" ") { it.lowercase().replaceFirstChar(Char::titlecase) } ?: getString(default)
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
fun <T> List<T>.toDisplayPairList(context: Fragment, nameProvider: (T) -> String): List<Pair<T, String>> {
    return this.toDisplayPairList(context.requireActivity(), nameProvider)
}


/**
 * Generates a short offline development code based on the current date-time and a secret key.
 *
 * The code is generated by concatenating the current date-time formatted according to [dateFormat]
 * with the provided [secretKey], hashing the result with SHA-256, taking the first [hashLength]
 * characters of the hash, and converting them to uppercase or lowercase based on [uppercase].
 *
 * This can be useful for offline verification codes, time-based tokens, or similar scenarios.
 *
 * ```kotlin
 * //example
 * val secret = "YourSuperSecretKey"
 * val code = generateOfflineDevCode(secretKey = secret)
 * println(code) // e.g., "A1B2C3"
 *
 * val customCode = generateOfflineDevCode(secretKey = secret,dateFormat = "yyyyMMddHHmm",hashLength = 8,uppercase = false,locale = Locale.US)
 * println(customCode) // e.g., "a1b2c3d4"
 * ```
 *
 * @param secretKey The secret key used to salt the hash. Must be kept private.
 * @param dateFormat The pattern used to format the current date-time. Default is `"yyyyMMddHH"`.
 * @param hashLength The number of characters to take from the start of the hash. Default is `6`.
 * @param uppercase If true, the resulting code is converted to uppercase; otherwise lowercase. Default is `true`.
 * @param locale The [Locale] used for case conversion. Default is the system default locale.
 *
 * @return A short string representing the generated offline development code.
 *
 * @throws IllegalArgumentException if [hashLength] is less than 1.
 */
fun generateOfflineDevCode(secretKey: String, dateFormat: String = "yyyyMMddHH", hashLength: Int = 6, uppercase: Boolean = true, locale: Locale = Locale.getDefault()): String {
    require(hashLength > 0) { "hashLength must be greater than 0" }
    val currentDateTime = Date().toFormat(dateFormat, locale)
    val rawInput = currentDateTime + secretKey
    val hash = rawInput.sha256()
    val code = if (hash.length >= hashLength) {
        hash.take(hashLength)
    } else {
        hash
    }
    return if (uppercase) code.uppercase(locale) else code.lowercase(locale)
}


private fun isSimilarColor(color1: Int, color2: Int, tolerance: Int): Boolean {
    val r1 = Color.red(color1)
    val g1 = Color.green(color1)
    val b1 = Color.blue(color1)

    val r2 = Color.red(color2)
    val g2 = Color.green(color2)
    val b2 = Color.blue(color2)

    return (abs(r1 - r2) <= tolerance && abs(g1 - g2) <= tolerance && abs(b1 - b2) <= tolerance)
}


private fun String.sha256(): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(this.toByteArray())
    return hashBytes.joinToString("") { "%02x".format(it) }
}


private fun Date.toFormat(pattern: String = "yyyy-MM-dd", locale: Locale): String = synchronized(this) {
    SimpleDateFormat(pattern, locale).format(this)
}
