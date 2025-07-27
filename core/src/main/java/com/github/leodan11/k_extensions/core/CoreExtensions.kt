package com.github.leodan11.k_extensions.core

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Base64
import java.io.Serializable
import java.nio.charset.Charset
import androidx.core.graphics.createBitmap
import kotlin.math.abs
import androidx.core.graphics.set
import androidx.core.graphics.get


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


private fun isSimilarColor(color1: Int, color2: Int, tolerance: Int): Boolean {
    val r1 = Color.red(color1)
    val g1 = Color.green(color1)
    val b1 = Color.blue(color1)

    val r2 = Color.red(color2)
    val g2 = Color.green(color2)
    val b2 = Color.blue(color2)

    return (abs(r1 - r2) <= tolerance && abs(g1 - g2) <= tolerance && abs(b1 - b2) <= tolerance)
}