package com.github.leodan11.k_extensions.core

import android.util.Base64
import jahirfiquitiva.libs.textdrawable.TextDrawable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


/**
 * Generate avatars with initials from names.
 *
 * @param generator [ColorGenerator] default [ColorGenerator.MATERIAL]
 * @param config [TextDrawable.Builder] default [Unit]
 * @return [TextDrawable]
 */
fun String.asAvatar(
    generator: ColorGenerator = ColorGenerator.MATERIAL,
    config: TextDrawable.Builder.() -> Unit = {},
) = run {
    TextDrawable.build(this, generator.getColorBasedOnKey(this), config)
}


/**
 * Generate avatars with initials from names.
 *
 * @param generator [ColorGenerator] default [ColorGenerator.MATERIAL]
 * @param config [TextDrawable.Builder] default [Unit]
 * @return [TextDrawable]
 */
fun String.asAvatarRect(
    generator: ColorGenerator = ColorGenerator.MATERIAL,
    config: TextDrawable.Builder.() -> Unit = {},
) =
    run { TextDrawable.buildRect(this, generator.getColorBasedOnKey(this), config) }


/**
 * Generate avatars with initials from names.
 *
 * @param generator [ColorGenerator] default [ColorGenerator.MATERIAL]
 * @param config [TextDrawable.Builder] default [Unit]
 * @return [TextDrawable]
 */
fun String.asAvatarRound(
    generator: ColorGenerator = ColorGenerator.MATERIAL,
    config: TextDrawable.Builder.() -> Unit = {},
) =
    run { TextDrawable.buildRound(this, generator.getColorBasedOnKey(this), config) }


/**
 * Generate avatars with initials from names.
 *
 * @param radius [Int]
 * @param generator [ColorGenerator] default [ColorGenerator.MATERIAL]
 * @param config [TextDrawable.Builder] default [Unit]
 * @return [TextDrawable]
 */
fun String.asAvatarRoundRect(
    radius: Int,
    generator: ColorGenerator = ColorGenerator.MATERIAL,
    config: TextDrawable.Builder.() -> Unit = {},
) =
    run { TextDrawable.buildRoundRect(this, generator.getColorBasedOnKey(this), radius, config) }


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
 */
fun String.toBoolean(): Boolean {
    return this != "" &&
            (this.equals("TRUE", ignoreCase = true)
                    || this.equals("Y", ignoreCase = true)
                    || this.equals("YES", ignoreCase = true))
}


/**
 * Convert a text to a calendar
 *
 * @param pattern [String] by default yyyy-MM-dd
 * @return [Calendar]
 */
fun String.toCalendar(pattern: String = "yyyy-MM-dd"): Calendar = synchronized(this) {
    if (this.isEmpty()) throw Exception("Empty string, not date found")
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    val date = format.parse(this) ?: throw Exception("Wrong date")
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar
}


/**
 * Get the first two initial letters of a first and last name if it exists
 */
fun String.initials(): String =
    if (this.isNotBlank() && this.length < 3) this.uppercase()
    else {
        val sorts = this.split(' ')
        when (sorts.size) {
            1 -> sorts.first().substring(0, 2).uppercase()
            2 -> "${sorts.first().substring(0, 1).uppercase()}${
                sorts.last().substring(0, 1).uppercase()
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
        result.toString()
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        this
    }
}