package com.github.leodan11.k_extensions.calendar

import android.util.Log
import com.github.leodan11.k_extensions.calendar.base.DatePatternConfig
import com.github.leodan11.k_extensions.calendar.model.DateTimeResult
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

/* ---------- legacy → java.time ---------- */

/**
 * Converts a legacy [Date] to a [Instant].
 *
 * @receiver The [Date] instance to convert.
 *
 *
 * ```kotlin
 * val date = Date()
 * val instant: Instant = date.toInstantCompat()
 * println(instant) // e.g., 2026-01-05T12:34:56.789Z
 * ```
 * @return The corresponding [Instant] representing the same point in time.
 * @since 2.2.6
 */
fun Date.toInstantCompat(): Instant = Instant.ofEpochMilli(time)


/* ---------- java.time → legacy ---------- */

/**
 * Converts a [ZonedDateTime] to a [Calendar] instance.
 *
 * @receiver The [ZonedDateTime] to convert.
 *
 * ```kotlin
 * val zdt = ZonedDateTime.now()
 * val calendar: Calendar = zdt.toCalendar()
 * println(calendar.time)
 * ```
 * @return A [Calendar] representing the same instant and time zone.
 * @since 2.2.6
 */
fun ZonedDateTime.toCalendar(): Calendar = GregorianCalendar.from(this)

/**
 * Converts an [Instant] to a [Date].
 *
 * @receiver The [Instant] to convert.
 *
 * ```kotlin
 * val instant = Instant.now()
 * val date: Date = instant.toDateCompat()
 * println(date)
 * ```
 * @return A [Date] representing the same instant.
 * @since 2.2.6
 */
fun Instant.toDateCompat(): Date = Date.from(this)


/* ---------- Calendar → java.time ---------- */

/**
 * Converts a [Calendar] to a [LocalDate] in its own time zone.
 * @since 2.2.6
 */
fun Calendar.toLocalDate(): LocalDate = Instant.ofEpochMilli(this.timeInMillis)
    .atZone(this.timeZone.toZoneId())
    .toLocalDate()

/**
 * Converts a [Calendar] to a [LocalTime] in its own time zone.
 * @since 2.2.6
 */
fun Calendar.toLocalTime(): LocalTime = Instant.ofEpochMilli(this.timeInMillis)
    .atZone(this.timeZone.toZoneId())
    .toLocalTime()

/**
 * Converts a [Calendar] to a [LocalDateTime] in its own time zone.
 * @since 2.2.6
 */
fun Calendar.toLocalDateTime(): LocalDateTime = Instant.ofEpochMilli(this.timeInMillis)
    .atZone(this.timeZone.toZoneId())
    .toLocalDateTime()

/**
 * Converts a [Calendar] to a [ZonedDateTime].
 * @since 2.2.6
 */
fun Calendar.toZonedDateTime(): ZonedDateTime = Instant.ofEpochMilli(this.timeInMillis)
    .atZone(this.timeZone.toZoneId())


/* ---------- Date → java.time ---------- */

/**
 * Converts a [Date] to a [LocalDate] in the system default time zone.
 * @since 2.2.6
 */
fun Date.toLocalDate(): LocalDate = Instant.ofEpochMilli(this.time)
    .atZone(ZoneId.systemDefault())
    .toLocalDate()

/**
 * Converts a [Date] to a [LocalDateTime] in the system default time zone.
 * @since 2.2.6
 */
fun Date.toLocalDateTime(): LocalDateTime = Instant.ofEpochMilli(this.time)
    .atZone(ZoneId.systemDefault())
    .toLocalDateTime()

/**
 * Converts a [Date] to a [ZonedDateTime] in the system default time zone.
 * @since 2.2.6
 */
fun Date.toZonedDateTime(): ZonedDateTime = Instant.ofEpochMilli(this.time)
    .atZone(ZoneId.systemDefault())


/* ---------- java.time → Date ---------- */

/**
 * Converts a [LocalDate] to a [Date] at start of day in the system default time zone.
 * @since 2.2.6
 */
fun LocalDate.toDate(): Date = Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())

/**
 * Converts a [LocalDateTime] to a [Date] in the system default time zone.
 * @since 2.2.6
 */
fun LocalDateTime.toDate(): Date = Date.from(this.atZone(ZoneId.systemDefault()).toInstant())

/**
 * Converts a [ZonedDateTime] to a [Date].
 * @since 2.2.6
 */
fun ZonedDateTime.toDate(): Date = Date.from(this.toInstant())


/**
 * Converts a string representing a date to a [Calendar] object using the specified [com.github.leodan11.k_extensions.calendar.base.DatePatternConfig].
 * If no config is provided, the default configuration will be used.
 *
 * @param datePatternConfig The configuration containing date patterns and their formats.
 * Default is [DatePatternConfig.default()], which includes common patterns.
 * @return A [Calendar] object representing the parsed date.
 * @throws Exception If the input string is empty.
 * @throws IllegalArgumentException If the input string doesn't match any of the available patterns.
 * @since 2.2.6
 */
fun String.toCalendar(datePatternConfig: DatePatternConfig = DatePatternConfig.default()): Calendar {
    if (this.isEmpty()) throw Exception("Empty string, not date found")
    val inferredPattern = datePatternConfig.inferDatePattern(this)
    Log.i("StringExtensions", "Pattern: $inferredPattern")
    val matchedPattern = datePatternConfig.getPatterns().find { this.matches(Regex(it.first)) } ?: throw IllegalArgumentException("Input string doesn't match any available pattern")
    return this.toCalendarSimpleFormat(matchedPattern.second)
}


/**
 * Converts a string representing a date to a [Calendar] object using the specified [com.github.leodan11.k_extensions.calendar.base.DatePatternConfig] and locale.
 * If no config is provided, the default configuration will be used.
 *
 * @param datePatternConfig The configuration containing date patterns and their formats.
 * Default is [DatePatternConfig.default()], which includes common patterns.
 * @param locale The [Locale] used for formatting.
 * @return A [Calendar] object representing the parsed date.
 * @throws Exception If the input string is empty.
 * @throws IllegalArgumentException If the input string doesn't match any of the available patterns.
 * @since 2.2.6
 */
fun String.toCalendar(datePatternConfig: DatePatternConfig = DatePatternConfig.default(), locale: Locale): Calendar {
    if (this.isEmpty()) throw Exception("Empty string, not date found")
    val inferredPattern = datePatternConfig.inferDatePattern(this)
    Log.i("StringExtensions", "Pattern: $inferredPattern")
    val matchedPattern = datePatternConfig.getPatterns().find { this.matches(Regex(it.first)) } ?: throw IllegalArgumentException("Input string doesn't match any available pattern")
    return this.toCalendarSimpleFormat(matchedPattern.second, locale)
}



/**
 * Converts a string representing a date/time to a [DateTimeResult] based on its pattern.
 *
 * Uses [com.github.leodan11.k_extensions.calendar.base.DatePatternConfig] to infer the format.
 *
 * @param datePatternConfig Configuration containing patterns and their formats. Defaults to DatePatternConfig.default().
 * @param locale The locale used for formatting.
 * @return A [DateTimeResult] representing the parsed date/time.
 * @throws IllegalArgumentException if the string is empty or doesn't match any pattern.
 * @since 2.2.6
 */
fun String.toJavaTimeTyped(
    datePatternConfig: DatePatternConfig = DatePatternConfig.default(),
    locale: Locale = Locale.getDefault()
): DateTimeResult {
    if (this.isEmpty()) throw IllegalArgumentException("Empty string, not date found")

    val inferredPattern = datePatternConfig.inferDatePattern(this)
    val formatter = DateTimeFormatter.ofPattern(inferredPattern, locale)

    return try {
        when {
            // Pattern has offset info, return OffsetDateTime
            inferredPattern.contains("XXX") -> DateTimeResult.OffsetDateTimeResult(OffsetDateTime.parse(this, formatter))

            // Pattern has time (hours, minutes, seconds) and date -> LocalDateTime
            inferredPattern.contains("H") && inferredPattern.contains("m") && inferredPattern.contains("s") && inferredPattern.contains("y") ->
                DateTimeResult.DateTime(LocalDateTime.parse(this, formatter))

            // Pattern is only time -> LocalTime
            inferredPattern.matches(Regex("\\d{2}:\\d{2}:\\d{2}")) -> DateTimeResult.Time(LocalTime.parse(this, formatter))

            // Pattern is only date -> LocalDate
            inferredPattern.matches(Regex("\\d{4}-\\d{2}-\\d{2}")) || inferredPattern.matches(Regex("\\d{2}/\\d{2}/\\d{4}")) ->
                DateTimeResult.Date(LocalDate.parse(this, formatter))

            // Default fallback -> LocalDateTime
            else -> DateTimeResult.DateTime(LocalDateTime.parse(this, formatter))
        }
    } catch (e: DateTimeParseException) {
        throw IllegalArgumentException("Failed to parse '$this' with pattern '$inferredPattern'", e)
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
 * @since 2.2.6
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
 * @since 2.2.6
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
 * Converts a [String] to a [LocalDate] using the specified pattern.
 *
 * ```
 * val date = "2026-01-05".toLocalDate("yyyy-MM-dd")
 * println(date) // 2026-01-05
 * ```
 *
 * @receiver The text representing a date.
 * @param pattern The date pattern to parse. Defaults to "yyyy-MM-dd".
 * @return The parsed [LocalDate].
 * @throws IllegalArgumentException if the string is empty or cannot be parsed.
 * @since 2.2.6
 */
@Throws(IllegalArgumentException::class)
fun String.toLocalDate(pattern: String = "yyyy-MM-dd"): LocalDate {
    return this.toLocalDate(pattern = pattern, locale = Locale.getDefault())
}

/**
 * Converts a [String] to a [LocalDate] using the specified pattern.
 *
 * ```
 * val date = "2026-01-05".toLocalDate("yyyy-MM-dd")
 * println(date) // 2026-01-05
 * ```
 *
 * @receiver The text representing a date.
 * @param pattern The date pattern to parse. Defaults to "yyyy-MM-dd".
 * @param locale The [Locale] to use for parsing. Defaults to the system default.
 * @return The parsed [LocalDate].
 * @throws IllegalArgumentException if the string is empty or cannot be parsed.
 * @since 2.2.6
 */
@Throws(IllegalArgumentException::class)
fun String.toLocalDate(pattern: String = "yyyy-MM-dd", locale: Locale): LocalDate {
    if (this.isEmpty()) throw IllegalArgumentException("Empty string, no date found")
    return try {
        val formatter = DateTimeFormatter.ofPattern(pattern, locale)
        LocalDate.parse(this, formatter)
    } catch (e: DateTimeParseException) {
        throw IllegalArgumentException("Wrong date format: $this")
    }
}


/**
 * Converts this string to a [LocalDateTime] using the specified [pattern].
 * Uses the system default locale.
 *
 * ```kotlin
 * val dateTimeStr = "2026-01-05 14:30:00"
 * val dateTime = dateTimeStr.toLocalDateTime()
 * println(dateTime) // 2026-01-05T14:30
 * ```
 *
 * @receiver The string representing a date-time.
 * @param pattern The pattern to parse the string. Default is "yyyy-MM-dd HH:mm:ss".
 * @return The parsed [LocalDateTime].
 * @throws IllegalArgumentException If the string is empty or does not match the pattern.
 * @since 2.2.6
 */
fun String.toLocalDateTime(pattern: String = "yyyy-MM-dd HH:mm:ss"): LocalDateTime {
    return this.toLocalDateTime(pattern = pattern, locale = Locale.getDefault())
}

/**
 * Converts this string to a [LocalDateTime] using the specified [pattern] and [locale].
 *
 * ```kotlin
 * val dateTimeStr = "05/01/2026 14:30"
 * val dateTime = dateTimeStr.toLocalDateTime("dd/MM/yyyy HH:mm", Locale("es", "ES"))
 * println(dateTime) // 2026-01-05T14:30
 * ```
 *
 * @receiver The string representing a date-time.
 * @param pattern The pattern to parse the string.
 * @param locale The [Locale] used for parsing.
 * @return The parsed [LocalDateTime].
 * @throws IllegalArgumentException If the string is empty or does not match the pattern.
 * @since 2.2.6
 */
fun String.toLocalDateTime(pattern: String = "yyyy-MM-dd HH:mm:ss", locale: Locale): LocalDateTime {
    if (this.isEmpty()) throw IllegalArgumentException("Empty string, no date-time found")
    return try {
        val formatter = DateTimeFormatter.ofPattern(pattern, locale)
        LocalDateTime.parse(this, formatter)
    } catch (_: DateTimeParseException) {
        throw IllegalArgumentException("Wrong date-time format: $this")
    }
}
