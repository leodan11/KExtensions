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
 * Extracts the main components of a full name string, typically the given name(s)
 * and one surname, depending on the [preferLastSurname] flag.
 *
 * Returns an empty string if the original string is blank or empty.
 *
 * The extraction logic is:
 * - If the name has 1 part, return it.
 * - If it has 2 parts, return both separated by a space.
 * - If it has 3 or more parts, return:
 *    - The first and second parts by default.
 *    - The first and last parts if [preferLastSurname] is true.
 *
 * This is useful for formatting or displaying names in a simplified way.
 *
 * @receiver The full name string to extract from.
 * @param preferLastSurname When true, prefers the last part as the surname component.
 *                         Defaults to false.
 * @return A string with the main name components extracted, or empty if none.
 *
 * ```kotlin
 * "Juan Carlos Pérez García".extractMainNameComponents()         // Returns "Juan Carlos"
 * "Juan Carlos Pérez García".extractMainNameComponents(true)     // Returns "Juan García"
 * "María".extractMainNameComponents()                            // Returns "María"
 * "".extractMainNameComponents()                                 // Returns ""
 * ```
 */
@JvmSynthetic
fun String.extractMainNameComponents(preferLastSurname: Boolean = false): String {
    if (this.isBlank()) return ""

    val parts = this.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }

    return when (parts.size) {
        1 -> parts[0]
        2 -> "${parts[0]} ${parts[1]}"
        3 -> if (preferLastSurname)
            "${parts[0]} ${parts[2]}"
        else
            "${parts[0]} ${parts[1]}"
        else -> if (preferLastSurname)
            "${parts[0]} ${parts.last()}"
        else
            "${parts[0]} ${parts[1]}"
    }
}



/**
 * Extracts initials from a text string with support for limits, separators, and full name logic.
 *
 * This function is useful for generating short representations (e.g., avatar labels) from names or text.
 *
 * - Trims the input and splits by whitespace.
 * - Returns the first character of selected words in uppercase.
 * - Allows using a separator (e.g., "." for "J.D.") or no separator (e.g., "JD").
 * - Can optionally limit the number of initials and choose between first two or first & last.
 * - For a single word, returns the first [wordLimit] letters uppercased, optionally separated.
 *
 * @param wordLimit The number of words or letters to extract initials from. Use `null` to extract from all words.
 * @param separator A string to place between initials (e.g., "." or "-").
 * @param containLastName If `true` and [wordLimit] is 2, uses first and last words instead of first two.
 *
 * Example:
 * ```
 * "John".initials()                             // "JO"
 * "John".initials(wordLimit = 1)               // "J"
 * "John Doe".initials()                         // "JD"
 * "John Doe Smith".initials()                   // "JS"
 * "John Doe Smith".initials(containLastName = true) // "JS"
 * "John A. Doe".initials(wordLimit = null)          // "JAD"
 * "John A. Doe".initials(wordLimit = null, separator = ".") // "J.A.D"
 * "John A. Doe".initials(wordLimit = 2, separator = "-", containLastName = true) // "J-D"
 * "John".initials(wordLimit = 3, separator = ".")  // "J.O.H"
 * ```
 *
 * @return A [String] of uppercase initials or letters with optional separator.
 */
fun String.initials(wordLimit: Int? = 2, separator: String = "", containLastName: Boolean = false): String {
    val trimmed = trim()
    if (trimmed.isEmpty()) return ""

    val parts = trimmed.split("\\s+".toRegex()).filter { it.isNotBlank() }

    return when {
        parts.size == 1 -> {
            val letters = parts.first().take(wordLimit ?: parts.first().length).map { it.uppercase() }
            letters.joinToString(separator)
        }

        wordLimit == null || wordLimit >= parts.size -> parts.mapNotNull { it.firstOrNull()?.uppercase() }.joinToString(separator)

        wordLimit == 1 -> parts.first().first().uppercase()

        wordLimit == 2 && containLastName && parts.size >= 2 -> listOf(parts.first(), parts.last()).mapNotNull { it.firstOrNull()?.uppercase() }.joinToString(separator)

        else -> parts.take(wordLimit).mapNotNull { it.firstOrNull()?.uppercase() }.joinToString(separator)
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
 * Returns a copy of this string with each word capitalized.
 *
 * Words are identified by spaces, and each word is converted to lowercase first,
 * then capitalized on the first character. Extra spaces are removed.
 *
 * This is useful for displaying properly formatted names, titles, or labels.
 *
 * Example:
 * ```
 * val raw = "   jOhN    doE  "
 * val formatted = raw.toCapitalizedPerWord()
 * // Result: "John Doe"
 * ```
 *
 * @return A new [String] with each word capitalized.
 */
fun String.toCapitalizedPerWord(): String {
    return trim().split("\\s+".toRegex()).joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { it.titlecaseChar() }
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