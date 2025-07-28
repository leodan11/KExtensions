package com.github.leodan11.k_extensions.string

import android.util.Base64
import androidx.annotation.ColorInt
import com.github.leodan11.k_extensions.base.ShapeTextDrawable
import com.github.leodan11.k_extensions.string.content.DatePatternConfig
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


/**
 * Generate avatars with initials from names.
 *
 * For more information about [TextDrawable](https://github.com/jahirfiquitiva/TextDrawable).
 *
 * @param color [Int]
 * @param config [ShapeTextDrawable.Builder] default [Unit]
 *
 * @return [ShapeTextDrawable]
 *
 */
fun String.asAvatar(
    @ColorInt color: Int,
    config: ShapeTextDrawable.Builder.() -> Unit = {},
) = ShapeTextDrawable.build(this, color, config)


/**
 * Generate avatars with initials from names.
 *
 * For more information about [TextDrawable](https://github.com/jahirfiquitiva/TextDrawable).
 *
 * @param color [Int]
 * @param config [ShapeTextDrawable.Builder] default [Unit]
 *
 * @return [ShapeTextDrawable]
 *
 */
fun String.asAvatarRect(
    @ColorInt color: Int,
    config: ShapeTextDrawable.Builder.() -> Unit = {},
) = ShapeTextDrawable.buildRect(this, color, config)


/**
 * Generate avatars with initials from names.
 *
 * For more information about [TextDrawable](https://github.com/jahirfiquitiva/TextDrawable).
 *
 * @param color [Int]
 * @param config [ShapeTextDrawable.Builder] default [Unit]
 *
 * @return [ShapeTextDrawable]
 *
 */
fun String.asAvatarRound(
    @ColorInt color: Int,
    config: ShapeTextDrawable.Builder.() -> Unit = {},
) = ShapeTextDrawable.buildRound(this, color, config)


/**
 * Generate avatars with initials from names.
 *
 * For more information about [TextDrawable](https://github.com/jahirfiquitiva/TextDrawable).
 *
 * @param radius [Int]
 * @param color [Int]
 * @param config [ShapeTextDrawable.Builder] default [Unit]
 *
 * @return [ShapeTextDrawable]
 *
 */
fun String.asAvatarRoundRect(
    radius: Int,
    @ColorInt color: Int,
    config: ShapeTextDrawable.Builder.() -> Unit = {},
) = ShapeTextDrawable.buildRoundRect(this, color, radius, config)


/**
 * Convert a string as consecutive code
 *
 * @param length default 5
 * @param char default 0
 *
 * @return [String] code, e.g: 00002
 *
 */
fun String.asConsecutiveCode(length: Int = 5, char: Char = '0'): String =
    this.padStart(length, char)


/**
 * String to Base64
 *
 * @param flags By default [Base64.DEFAULT]
 *
 * @return [ByteArray]
 *
 */
fun String.toBase64Decode(flags: Int = Base64.DEFAULT): ByteArray = Base64.decode(this, flags)


/**
 * Converts a string to boolean such as 'Y', 'yes', 'TRUE'
 *
 * @return [Boolean]
 *
 */
fun String.toBoolean(): Boolean {
    return this.isNotEmpty() &&
            (this.equals("TRUE", ignoreCase = true)
                    || this.equals("Y", ignoreCase = true)
                    || this.equals("YES", ignoreCase = true))
}


/**
 * Converts a string representing a date to a [Calendar] object using the specified [DatePatternConfig].
 * If no config is provided, the default configuration will be used.
 *
 * @param datePatternConfig The configuration containing date patterns and their formats.
 * Default is [DatePatternConfig.default()], which includes common patterns.
 * @return A [Calendar] object representing the parsed date.
 * @throws Exception If the input string is empty.
 * @throws IllegalArgumentException If the input string doesn't match any of the available patterns.
 */
fun String.toCalendar(datePatternConfig: DatePatternConfig = DatePatternConfig.default()): Calendar {
    if (this.isEmpty()) throw Exception("Empty string, not date found")
    val inferredPattern = datePatternConfig.inferDatePattern(this)
    val matchedPattern = datePatternConfig.getPatterns().find { this.matches(Regex(it.first)) }
        ?: throw IllegalArgumentException("Input string doesn't match any available pattern")
    return this.toCalendarSimpleFormat(matchedPattern.second)
}

/**
 * Converts a string representing a date to a [Calendar] object using the specified [DatePatternConfig] and locale.
 * If no config is provided, the default configuration will be used.
 *
 * @param datePatternConfig The configuration containing date patterns and their formats.
 * Default is [DatePatternConfig.default()], which includes common patterns.
 * @param locale The [Locale] used for formatting.
 * @return A [Calendar] object representing the parsed date.
 * @throws Exception If the input string is empty.
 * @throws IllegalArgumentException If the input string doesn't match any of the available patterns.
 */
fun String.toCalendar(
    datePatternConfig: DatePatternConfig = DatePatternConfig.default(),
    locale: Locale
): Calendar {
    if (this.isEmpty()) throw Exception("Empty string, not date found")
    val inferredPattern = datePatternConfig.inferDatePattern(this)
    val matchedPattern = datePatternConfig.getPatterns().find { this.matches(Regex(it.first)) }
        ?: throw IllegalArgumentException("Input string doesn't match any available pattern")
    return this.toCalendarSimpleFormat(matchedPattern.second, locale)
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
 * Convert a text to a calendar
 *
 * @param pattern [String] default `yyyy-MM-dd`
 * @param locale The [Locale] to apply for formatting.
 *
 * @return [Calendar]
 *
 * @throws IllegalArgumentException
 *
 */
fun String.toCalendarSimpleFormat(pattern: String = "yyyy-MM-dd", locale: Locale): Calendar =
    synchronized(this) {
        if (this.isEmpty()) throw IllegalArgumentException("Empty string, not date found")
        val format = SimpleDateFormat(pattern, locale)
        val date = format.parse(this) ?: throw IllegalArgumentException("Wrong date")
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar
    }


/**
 * Get the first two initial letters of a first and last name if it exists
 *
 * @param containLastName default false
 *
 * @return [String]
 *
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
 *
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
 * To write or print with an initial capital
 *
 * @param locale The [Locale] to apply for formatting.
 * @return [String] Converted string or unchanged value if it generates an error
 *
 */
fun String.toCapitalize(locale: Locale): String {
    return try {
        this.replaceFirstChar { it.titlecase(locale) }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        this
    }
}


/**
 * Write or print each word with a capital letter
 *
 * @return [String] Converted string or unchanged value if it generates an error
 *
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
 *
 */
val String.isAlphanumeric get() = matches("^[a-zA-Z0-9]*$".toRegex())


/**
 * Check if a string is alphabetic
 *
 * @return [Boolean] - `true` or `false`
 *
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


/**
 * Removes duplicates words from a string. Words are separated by one of more space characters.
 * @return String with duplicate words removed
 */
fun String.uniquifyWords(): String {
    val multipleSpacesRegex = " +".toRegex()
    return this.split(multipleSpacesRegex).distinct().joinToString(" ")
}