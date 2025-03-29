package com.github.leodan11.k_extensions.core

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.util.Base64
import java.io.Serializable
import java.nio.charset.Charset
import androidx.core.graphics.createBitmap


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

    val combined = createBitmap(width, height, bitmap.config)
    val canvas = Canvas(combined)
    val canvasW = canvas.width
    val canvasH = canvas.height
    canvas.drawBitmap(bitmap, Matrix(), null)

    val centreX = ((canvasW - this.width) / 2).toFloat()
    val centreY = ((canvasH - this.height) / 2).toFloat()
    canvas.drawBitmap(this, centreX, centreY, null)

    return combined
}
