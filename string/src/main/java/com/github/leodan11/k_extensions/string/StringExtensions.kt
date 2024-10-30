package com.github.leodan11.k_extensions.string

import android.util.Base64
import androidx.annotation.ColorInt
import jahirfiquitiva.libs.textdrawable.TextDrawable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.regex.Pattern


/**
 * Generate avatars with initials from names.
 *
 * @param color [Int]
 * @param config [TextDrawable.Builder] default [Unit]
 * @return [TextDrawable]
 */
fun String.asAvatar(
    @ColorInt color: Int,
    config: TextDrawable.Builder.() -> Unit = {},
) = run {
    TextDrawable.build(this, color, config)
}


/**
 * Generate avatars with initials from names.
 *
 * @param color [Int]
 * @param config [TextDrawable.Builder] default [Unit]
 * @return [TextDrawable]
 */
fun String.asAvatarRect(
    @ColorInt color: Int,
    config: TextDrawable.Builder.() -> Unit = {},
) =
    run { TextDrawable.buildRect(this, color, config) }


/**
 * Generate avatars with initials from names.
 *
 * @param color [Int]
 * @param config [TextDrawable.Builder] default [Unit]
 * @return [TextDrawable]
 */
fun String.asAvatarRound(
    @ColorInt color: Int,
    config: TextDrawable.Builder.() -> Unit = {},
) =
    run { TextDrawable.buildRound(this, color, config) }


/**
 * Generate avatars with initials from names.
 *
 * @param radius [Int]
 * @param color [Int]
 * @param config [TextDrawable.Builder] default [Unit]
 * @return [TextDrawable]
 */
fun String.asAvatarRoundRect(
    radius: Int,
    @ColorInt color: Int,
    config: TextDrawable.Builder.() -> Unit = {},
) =
    run { TextDrawable.buildRoundRect(this, color, radius, config) }


/**
 * Convert a string as consecutive code
 *
 * @param length default 5
 * @param char default 0
 * @return [String] code, e.g: 00002
 */
fun String.asConsecutiveCode(length: Int = 5, char: Char = '0'): String =
    this.padStart(length, char)


/**
 * String to Base64
 *
 * @param flags By default [Base64.DEFAULT]
 * @return [ByteArray]
 */
fun String.toBase64Decode(flags: Int = Base64.DEFAULT): ByteArray = Base64.decode(this, flags)


/**
 * Converts a string to boolean such as 'Y', 'yes', 'TRUE'
 * @return [Boolean]
 */
fun String.toBoolean(): Boolean {
    return this.isNotEmpty() &&
            (this.equals("TRUE", ignoreCase = true)
                    || this.equals("Y", ignoreCase = true)
                    || this.equals("YES", ignoreCase = true))
}


/**
 * Convert a text to a calendar
 *
 * @param pattern [String] pattern default `^\\d{4}-\\d{2}-\\d{2}$`
 *
 * @return [Calendar]
 *
 * @throws IllegalArgumentException
 *
 */
fun String.toCalendar(pattern: String = "^\\d{4}-\\d{2}-\\d{2}$"): Calendar {
    if (this.isEmpty()) throw Exception("Empty string, not date found")
    val matcher: Pattern = Pattern.compile(pattern)
    return when {
        matcher.matcher(this).matches() -> this.toCalendarSimpleFormat()
        else -> throw IllegalArgumentException()
    }
}


/**
 * Convert a text to a calendar
 *
 * @param pattern [String] default `yyyy-MM-dd`
 *
 * @return [Calendar]
 *
 * @throws IllegalArgumentException
 *
 */
fun String.toCalendarSimpleFormat(pattern: String = "yyyy-MM-dd"): Calendar = synchronized(this) {
    if (this.isEmpty()) throw IllegalArgumentException("Empty string, not date found")
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    val date = format.parse(this) ?: throw IllegalArgumentException("Wrong date")
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar
}


/**
 * Get the first two initial letters of a first and last name if it exists
 * @param containLastName default false
 * @return [String]
 */
fun String.initials(containLastName: Boolean = false): String =
    if (this.isNotBlank() && this.length < 3) this.uppercase()
    else {
        val sorts = this.split(' ')
        when (sorts.size) {
            1 -> sorts.first().substring(0, 2).uppercase()
            2 -> "${sorts.first().substring(0, 1).uppercase()}${
                sorts.last().substring(0, 1).uppercase()
            }"

            3 -> if (containLastName) "${sorts.first().substring(0, 1).uppercase()}${
                sorts.last().substring(0, 1).uppercase()
            }" else "${sorts.first().substring(0, 1).uppercase()}${
                sorts[1].substring(0, 1).uppercase()
            }"

            else -> "${sorts.first().substring(0, 1).uppercase()}${
                sorts[2].substring(0, 1).uppercase()
            }"
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
        result.toString().trimEnd()
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        this
    }
}


/**
 * Check if a string is alphanumeric
 *
 * @return [Boolean] - `true` or `false`
 */
val String.isAlphanumeric get() = matches("^[a-zA-Z0-9]*$".toRegex())


/**
 * Check if a string is alphabetic
 *
 * @return [Boolean] - `true` or `false`
 */
val String.isAlphabetic get() = matches("^[a-zA-Z]*$".toRegex())


/**
 *
 * Search the most common character in a string
 *
 * @return [Char] - Most common character
 *
 */
val String.mostCommonCharacter: Char?
    get() {
        if (isEmpty()) return null
        val map = HashMap<Char, Int>()
        for (char in toCharArray()) map[char] = (map[char] ?: 0) + 1
        var maxEntry = map.entries.elementAt(0)
        for (entry in map) maxEntry = if (entry.value > maxEntry.value) entry else maxEntry
        return maxEntry.key
    }
