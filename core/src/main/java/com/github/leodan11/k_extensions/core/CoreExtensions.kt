package com.github.leodan11.k_extensions.core

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import java.io.Serializable
import java.nio.charset.Charset
import androidx.core.graphics.createBitmap
import kotlin.math.abs
import androidx.core.graphics.set
import androidx.core.graphics.get
import androidx.fragment.app.Fragment


/**
 * Extension method to get a specific type from a Bundle
 *
 * @receiver [Bundle]
 * @param key [String] - Key of the Bundle
 * @return [T] - Bundle value
 *
 * @see [Bundle.getSerializable]
 *
 */
inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}


/**
 * Extension method to get a specific type from an Intent
 *
 * @receiver [Intent]
 * @param key [String] - Key of the Intent
 * @return [T] - Intent value
 *
 * @see [Intent.getSerializableExtra]
 *
 */
inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
        key,
        T::class.java
    )

    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}


/**
 * Extension method to get the TAG name for all object
 */
fun <T : Any> T.tag() = this::class.simpleName


/**
 * Extension method to cast any object to a specific type
 *
 * @return [T] - Casted object
 *
 */
inline fun <reified T> Any.toCast(): T = if (this is T) this else this as T


/**
 * ByteArray decode to string
 *
 * @receiver [ByteArray]
 * @param flags By default [Base64.DEFAULT]
 * @return [String]
 *
 * @see [Base64.decode]
 *
 */
fun ByteArray.toBase64Decode(
    flags: Int = Base64.DEFAULT,
    charset: Charset = Charsets.UTF_8
): String =
    Base64.decode(this, flags).toString(charset)


/**
 * ByteArray encode to string
 *
 * @receiver [ByteArray]
 * @param flags By default [Base64.DEFAULT]
 * @return [String]
 *
 * @see [Base64.encodeToString]
 *
 */
fun ByteArray.toBase64Encode(flags: Int = Base64.DEFAULT): String =
    Base64.encodeToString(this, flags)


/**
 * ByteArray to string hexadecimal
 *
 * @receiver [ByteArray]
 * @param separator By default, empty String
 * @return [String]
 *
 */
fun ByteArray.toHexString(separator: String = ""): String =
    joinToString(separator) { "%02x".format(it) }


/**
 * Merge two bitmaps
 *
 * @receiver [Bitmap]
 * @param bitmap [Bitmap] to merge
 * @return [Bitmap] merged bitmap
 *
 * @see [Bitmap.createBitmap]
 *
 */
fun Bitmap.mergeBitmaps(bitmap: Bitmap): Bitmap {
    val height = bitmap.height
    val width = bitmap.width

    val combined = createBitmap(width, height, bitmap.config ?: Bitmap.Config.ARGB_8888)
    val canvas = Canvas(combined)
    val canvasW = canvas.width
    val canvasH = canvas.height
    canvas.drawBitmap(bitmap, Matrix(), null)

    val centreX = ((canvasW - this.width) / 2).toFloat()
    val centreY = ((canvasH - this.height) / 2).toFloat()
    canvas.drawBitmap(this, centreX, centreY, null)

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


private fun isSimilarColor(color1: Int, color2: Int, tolerance: Int): Boolean {
    val r1 = Color.red(color1)
    val g1 = Color.green(color1)
    val b1 = Color.blue(color1)

    val r2 = Color.red(color2)
    val g2 = Color.green(color2)
    val b2 = Color.blue(color2)

    return (abs(r1 - r2) <= tolerance && abs(g1 - g2) <= tolerance && abs(b1 - b2) <= tolerance)
}