package com.github.leodan11.k_extensions.core

import android.util.Base64
import java.util.Locale

/**
 * String to Base64
 *
 * @param flags By default [Base64.DEFAULT]
 * @return [ByteArray]
 */
fun String.toBase64Decode(flags: Int = Base64.DEFAULT): ByteArray = Base64.decode(this, flags)

/**
 * Converts a string to boolean such as 'Y', 'yes', 'TRUE'
 */

fun String.toBoolean(): Boolean {
    return this != "" &&
            (this.equals("TRUE", ignoreCase = true)
                    || this.equals("Y", ignoreCase = true)
                    || this.equals("YES", ignoreCase = true))
}

/**
 * Get the first 2 initial letters of a first and last name if it exists
 */
val String.initials: String
    get() {
        return if (this.isNotBlank() && this.length < 3) this.uppercase()
        else {
            val sorts = this.split(' ')
            when (sorts.size) {
                1 -> sorts.first().substring(0, 2).uppercase()
                2 -> "${sorts.first().substring(0, 1).uppercase()}${sorts.last().substring(0, 1).uppercase()}"
                else -> "${sorts.first().substring(0, 1).uppercase()}${sorts[2].substring(0, 1).uppercase()}"
            }
        }
    }


/**
 * To write or print with an initial capital
 *
 * @return [String] Converted string or unchanged value if it generates an error
 */
fun String.toCapitalize(): String {
    return try {
        this.replaceFirstChar { it.titlecase(Locale.getDefault()) }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        this
    }
}

/**
 * Write or print each word with a capital letter
 *
 * @return [String] Converted string or unchanged value if it generates an error
 */
fun String.toCapitalizePerWord(): String {
    return try {
        val srt = this.lowercase()
        val parts = srt.split(' ')
        val result = StringBuilder()
        for (item in parts) {
            result.append(item.toCapitalize())
            result.append(" ")
        }
        result.toString()
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        this
    }
}