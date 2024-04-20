package com.github.leodan11.k_extensions.core

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.Base64
import android.util.Log
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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


/**
 * Calendar to String format
 *
 * @param typeCast [DateFormat]. By default [DateFormat.SHORT]
 * @return [String] e.g. 7/4/01
 */
fun Calendar.formatDate(typeCast: Int = DateFormat.SHORT): String =
    synchronized(this) { DateFormat.getDateInstance(typeCast).format(this.time) }


/**
 * Calendar to String format
 *
 * @param typeCastDate [DateFormat]. By default [DateFormat.SHORT]
 * @param typeCastTime [DateFormat]. By default [DateFormat.MEDIUM]
 * @return [String] a date/time formatter, e.g. 7/4/01, 3:53:52 PM
 */
fun Calendar.formatDateTime(
    typeCastDate: Int = DateFormat.SHORT,
    typeCastTime: Int = DateFormat.MEDIUM,
): String = synchronized(this) {
    DateFormat.getDateTimeInstance(typeCastDate, typeCastTime).format(this.time)
}


/**
 * Calendar to String format
 *
 * @param typeCast [DateFormat]. By default [DateFormat.SHORT]
 * @return e.g. 3:30 PM
 */
fun Calendar.formatTime(typeCast: Int = DateFormat.SHORT): String = synchronized(this) {
    DateFormat.getTimeInstance(typeCast).format(this.time)
}


/**
 * Calendar to String format
 *
 * @param pattern [String]. By default yyyy-MM-dd
 * @return [String] e.g. 2001/07/04
 */
fun Calendar.toFormat(pattern: String = "yyyy-MM-dd"): String = synchronized(this) {
    SimpleDateFormat(pattern, Locale.getDefault()).format(this.time)
}


/**
 * Copy file
 *
 * @param toFile [FileOutputStream]
 */
fun FileInputStream.copyFile(toFile: FileOutputStream) {
    try {
        var fromChannel: FileChannel? = null
        var toChannel: FileChannel? = null

        try {
            fromChannel = this.channel
            toChannel = toFile.channel

            fromChannel.transferTo(0, fromChannel.size(), toChannel)
        } finally {
            try {
                fromChannel?.close()
            } finally {
                toChannel?.close()
            }
        }
        Log.i("Copy File", "Completed")
    } catch (exception: Exception) {
        exception.printStackTrace()
        throw Exception("Exception copy File")
    }
}