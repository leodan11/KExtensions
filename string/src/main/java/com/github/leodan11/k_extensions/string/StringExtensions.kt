package com.github.leodan11.k_extensions.string

import android.util.Base64
import android.util.Log
import androidx.annotation.ColorInt
import com.github.leodan11.k_extensions.base.ShapeTextDrawable
import com.github.leodan11.k_extensions.string.content.DatePatternConfig
import com.github.leodan11.k_extensions.string.content.HashFormat
import java.nio.charset.Charset
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val ALPHABETIC_REGEX = Regex("^[a-zA-Z]*$")
private val ALPHANUMERIC_REGEX = Regex("^[a-zA-Z0-9]*$")


/**
 * Checks whether the string contains only alphabetic characters (letters a-z, A-Z).
 *
 * This excludes digits, symbols, punctuation, whitespace, or special characters.
 *
 * ```kotlin
 * "HelloWorld".isAlphabetic   // true
 * "Hello123".isAlphabetic     // false
 * "".isAlphabetic             // true (empty string is considered valid)
 * ```
 *
 * @return `true` if the string consists only of letters, or is empty; `false` otherwise.
 */
val String.isAlphabetic: Boolean
    get() = ALPHABETIC_REGEX.matches(this)


/**
 * Returns the negation of [isAlphabetic].
 *
 * @see isAlphabetic
 */
val String.isNotAlphabetic: Boolean
    get() = !isAlphabetic


/**
 * Checks whether the string contains only alphanumeric characters (letters and digits).
 *
 * This excludes symbols, punctuation, whitespace, or special characters.
 *
 * ```kotlin
 * "abc123".isAlphanumeric    // true
 * "abc_123".isAlphanumeric   // false
 * "".isAlphanumeric          // true
 * ```
 *
 * @return `true` if the string consists only of letters (a-z, A-Z) and digits (0-9), or is empty; `false` otherwise.
 *
 */
val String.isAlphanumeric: Boolean
    get() = ALPHANUMERIC_REGEX.matches(this)


/**
 * Returns the negation of [isAlphanumeric].
 *
 * @see isAlphanumeric
 */
val String.isNotAlphanumeric: Boolean
    get() = !isAlphanumeric


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
fun String.asAvatar(@ColorInt color: Int, config: ShapeTextDrawable.Builder.() -> Unit = {}): ShapeTextDrawable {
    return ShapeTextDrawable.build(this, color, config)
}


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
fun String.asAvatarRect(@ColorInt color: Int, config: ShapeTextDrawable.Builder.() -> Unit = {}): ShapeTextDrawable {
    return ShapeTextDrawable.buildRect(this, color, config)
}


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
fun String.asAvatarRound(@ColorInt color: Int, config: ShapeTextDrawable.Builder.() -> Unit = {}): ShapeTextDrawable {
    return ShapeTextDrawable.buildRound(this, color, config)
}


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
fun String.asAvatarRoundRect(radius: Int, @ColorInt color: Int, config: ShapeTextDrawable.Builder.() -> Unit = {}): ShapeTextDrawable {
    return ShapeTextDrawable.buildRoundRect(this, color, radius, config)
}


/**
 * Pads this [String] on the left with the specified [paddingChar] until it reaches the desired [length].
 *
 * Commonly used for formatting numeric codes or identifiers to a fixed length.
 *
 * ```kotlin
 * "42".toFixedLengthCode()           // "00042"
 * "123".toFixedLengthCode(6, 'X')    // "XXX123"
 * "98765".toFixedLengthCode(4)       // "98765" (no truncation, returned as-is)
 * ```
 *
 * @param length The total desired length of the output string. If the string is already this length or longer, it is returned as-is.
 * @param paddingChar The character to pad with. Default is `'0'`.
 * @return A new string padded to the specified [length] using [paddingChar] on the left.
 */
fun String.toFixedLengthCode(length: Int = 5, paddingChar: Char = '0'): String = this.padStart(length, paddingChar)


/**
 * Generates a hexadecimal hash string from this [String] using the specified [algorithm].
 *
 * @param algorithm The hashing algorithm to use. Common values include:
 * - `"SHA-256"` (default)
 * - `"SHA-1"`
 * - `"SHA-512"`
 * - `"MD5"`
 *
 * ```kotlin
 * val hash = "hello".hash("SHA-512")
 * println(hash)
 * ```
 *
 * @return The resulting hash as a lowercase hexadecimal string.
 *
 * @throws IllegalArgumentException if the algorithm is not supported.
 */
fun String.hash(algorithm: String = "SHA-256"): String {
    val digest = MessageDigest.getInstance(algorithm)
    val hashBytes = digest.digest(this.toByteArray())
    return hashBytes.joinToString("") { "%02x".format(it) }
}


/**
 * Generates a hash from this [String] using the specified [algorithm] and returns it in the selected [format].
 *
 * @param algorithm The hashing algorithm to use. Default is `"SHA-256"`.
 *                  Common values: `"SHA-1"`, `"SHA-256"`, `"SHA-512"`, `"MD5"`.
 * @param format The output format: either [HashFormat.HEX] or [HashFormat.BASE64]. Default is HEX.
 * @param charset The charset to use when converting the string to bytes. Default is [Charsets.UTF_8].
 * @param base64Flags Flags to control Base64 encoding. Only used if [format] is [HashFormat.BASE64].
 *                    Defaults to [Base64.NO_WRAP].
 *
 * @return The resulting hash string in the specified format.
 *
 * ### Examples:
 * ```kotlin
 * val hash1 = "hello".hash("SHA-512") // hex by default
 * val hash2 = "hello".hash("MD5", format = HashFormat.BASE64)
 * val hash3 = "hello".hash("SHA-1", charset = Charsets.UTF_16)
 * ```
 */
fun String.hash(algorithm: String = "SHA-256", format: HashFormat = HashFormat.HEX, charset: Charset = Charsets.UTF_8, base64Flags: Int = Base64.NO_WRAP): String {
    val digest = MessageDigest.getInstance(algorithm)
    val hashBytes = digest.digest(this.toByteArray(charset))
    return when (format) {
        HashFormat.HEX -> hashBytes.joinToString("") { "%02x".format(it) }
        HashFormat.BASE64 -> Base64.encodeToString(hashBytes, base64Flags)
    }
}


/**
 * Computes the SHA-256 hash of this string and returns it as a hexadecimal string.
 *
 * This extension uses the `MessageDigest` class to generate a SHA-256 hash,
 * which is a cryptographic hash function that produces a 256-bit (32-byte) hash value.
 * The resulting hash bytes are converted to a lowercase hexadecimal string.
 *
 * ```kotlin
 * //example
 * val input = "Hello, World!"
 * val hash = input.sha256()
 * println(hash) // Output: "a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e"
 * ```
 *
 * @receiver The input [String] to be hashed.
 * @return A [String] representing the SHA-256 hash in hexadecimal format.
 *
 * @throws java.security.NoSuchAlgorithmException if SHA-256 algorithm is not available on the platform.
 */
fun String.sha256(): String {
    return this.hash()
}


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
    Log.i("StringExtensions", "Pattern: $inferredPattern")
    val matchedPattern = datePatternConfig.getPatterns().find { this.matches(Regex(it.first)) } ?: throw IllegalArgumentException("Input string doesn't match any available pattern")
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
fun String.toCalendar(datePatternConfig: DatePatternConfig = DatePatternConfig.default(), locale: Locale): Calendar {
    if (this.isEmpty()) throw Exception("Empty string, not date found")
    val inferredPattern = datePatternConfig.inferDatePattern(this)
    Log.i("StringExtensions", "Pattern: $inferredPattern")
    val matchedPattern = datePatternConfig.getPatterns().find { this.matches(Regex(it.first)) } ?: throw IllegalArgumentException("Input string doesn't match any available pattern")
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
 * Checks whether this [String] matches the given [Regex] pattern.
 *
 * ```kotlin
 * "hello123".matchesPattern(Regex("^[a-zA-Z0-9]+$")) // true
 * "hello!".matchesPattern(Regex("^[a-zA-Z]+$"))      // false
 * ```
 *
 * @param pattern A compiled [Regex] to match against the string.
 * @return `true` if the string matches the pattern; `false` otherwise.
 */
fun String.matchesPattern(pattern: Regex): Boolean = pattern.matches(this)


/**
 * Checks whether this [String] matches the given regex pattern provided as a [String].
 *
 * ```kotlin
 * "42".matchesPattern("^\\d+$")  // true
 * "abc".matchesPattern("^\\d+$") // false
 * ```
 *
 * @param pattern A regex pattern in string format.
 * @return `true` if the string matches the pattern; `false` otherwise.
 */
fun String.matchesPattern(pattern: String): Boolean = Regex(pattern).matches(this)


/**
 * Checks whether this [String] does NOT match the given [Regex] pattern.
 *
 * @param pattern A compiled [Regex] to match against the string.
 * @return `true` if the string does NOT match the pattern; `false` if it does.
 */
fun String.doesNotMatchPattern(pattern: Regex): Boolean = !this.matches(pattern)


/**
 * Checks whether this [String] does NOT match the given regex pattern string.
 *
 * @param pattern A regex pattern in string format.
 * @return `true` if the string does NOT match the pattern; `false` if it does.
 */
fun String.doesNotMatchPattern(pattern: String): Boolean = !this.matches(Regex(pattern))


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