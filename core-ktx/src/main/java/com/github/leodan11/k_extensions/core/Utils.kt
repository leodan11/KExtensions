package com.github.leodan11.k_extensions.core

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.Base64

/**
 * Merge bitmaps
 *
 * @param bitmap
 * @return [Bitmap]
 */
fun Bitmap.mergeBitmaps(bitmap: Bitmap): Bitmap {
    val height = bitmap.height
    val width = bitmap.width

    val combined = Bitmap.createBitmap(width, height, bitmap.config)
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
 * ByteArray encode to string
 *
 * @param flags By default [Base64.DEFAULT]
 * @return [String]
 */
fun ByteArray.toBase64Encode(flags: Int = Base64.DEFAULT): String =
    Base64.encodeToString(this, flags)


/**
 * ByteArray to string hexadecimal
 *
 * @param separator By default, empty String
 * @return [String]
 */
fun ByteArray.toHexString(separator: String = ""): String =
    joinToString(separator) { "%02x".format(it) }

