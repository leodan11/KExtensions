package com.github.leodan11.k_extensions.calendar

import java.time.Duration
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.FormatStyle.MEDIUM
import java.time.format.FormatStyle.SHORT
import java.time.format.TextStyle
import java.util.Locale


/**
 * Returns the current date at midnight in the given timezone.
 *
 * ```kotlin
 * val midnightZoned = ZonedDateTime.todayAtMidnight(ZoneId.of("America/Mexico_City"))
 * println(midnightZoned) // e.g., 2026-01-05T00:00-06:00[America/Mexico_City]
 * ```
 *
 * @param zone The [ZoneId] to apply. Defaults to system default timezone.
 * @return [ZonedDateTime] at 00:00 in the given timezone.
 * @since 2.2.6
 */
fun ZonedDateTime.todayAtMidnight(zone: ZoneId): ZonedDateTime =
    LocalDate.now(zone).atStartOfDay(zone)

/**
 * Shortcut property for today's midnight in system default timezone.
 * @since 2.2.6
 */
val ZonedDateTime.todayAtMidnight: ZonedDateTime
    get() = todayAtMidnight(ZoneId.systemDefault())

/**
 * Returns a [ZonedDateTime] at midnight (00:00) of this date in the same timezone.
 * @since 2.2.6
 */
fun ZonedDateTime.atMidnight(): ZonedDateTime = this.toLocalDate().atStartOfDay(this.zone)

/**
 * Checks if this [ZonedDateTime] is before another date based on month and year.
 * @since 2.2.6
 */
fun ZonedDateTime.isMonthBefore(other: ZonedDateTime?): Boolean =
    other?.let { YearMonth.from(this) < YearMonth.from(it) } ?: false

/**
 * Checks if this [ZonedDateTime] is after another date based on month and year.
 * @since 2.2.6
 */
fun ZonedDateTime.isMonthAfter(other: ZonedDateTime): Boolean =
    YearMonth.from(this) > YearMonth.from(other)

/**
 * Returns the localized name of the day of the week for this [ZonedDateTime].
 * @since 2.2.6
 */
fun ZonedDateTime.getDayName(): String =
    this.getDayName(locale = Locale.getDefault())

/**
 * Returns the localized name of the day of the week for this [ZonedDateTime].
 * @since 2.2.6
 */
fun ZonedDateTime.getDayName(locale: Locale): String =
    this.dayOfWeek.getDisplayName(TextStyle.FULL, locale)
        .replaceFirstChar { it.uppercase(locale) }

/**
 * Returns the month name and year in the format "MMMM yyyy".
 * @since 2.2.6
 */
fun ZonedDateTime.getMonthAndYearDate(): String = this.getMonthAndYearDate(locale = Locale.getDefault())

/**
 * Returns the month name and year in the format "MMMM yyyy".
 * @since 2.2.6
 */
fun ZonedDateTime.getMonthAndYearDate(locale: Locale): String =
    "${this.month.getDisplayName(TextStyle.FULL, locale)} ${this.year}"

/**
 * Returns a list of [ZonedDateTime] between this and [other], excluding start/end.
 * @since 2.2.6
 */
fun ZonedDateTime.rangeTo(other: ZonedDateTime): List<ZonedDateTime> {
    val result = mutableListOf<ZonedDateTime>()
    var current = this.plusDays(1)
    while (current.isBefore(other)) {
        result.add(current)
        current = current.plusDays(1)
    }
    return result
}

/**
 * Checks if this [ZonedDateTime] is between [start] and [end], inclusive.
 * @since 2.2.6
 */
fun ZonedDateTime.isBetween(start: ZonedDateTime, end: ZonedDateTime): Boolean =
    !this.isBefore(start) && !this.isAfter(end)

/**
 * Returns a human-readable elapsed time from this [ZonedDateTime] to now.
 * @since 2.2.6
 */
fun ZonedDateTime.toElapsedString(): String {
    val now = ZonedDateTime.now()
    val duration = Duration.between(this, now).abs()
    val days = duration.toDays()
    val hours = duration.toHours() % 24
    val minutes = duration.toMinutes() % 60
    val seconds = duration.seconds % 60
    return "$days days, $hours hours, $minutes minutes, $seconds seconds"
}


/**
 * Formats this [ZonedDateTime] using a custom [pattern].
 * The [Locale] to use for formatting. Defaults to system default.
 *
 * ```kotlin
 * val zoned = ZonedDateTime.now(ZoneId.of("America/Mexico_City"))
 * println(zoned.format()) // "2026-01-05 14:30:00 -06:00[America/Mexico_City]"
 * println(zoned.format("dd/MM/yyyy HH:mm z", Locale("es","ES"))) // "05/01/2026 14:30 CST"
 * ```
 *
 * @receiver The [ZonedDateTime] to format.
 * @param pattern The date-time pattern, default is "yyyy-MM-dd HH:mm:ss z".
 * @return The formatted date-time string.
 * @since 2.2.6
 */
fun ZonedDateTime.format(pattern: String = "yyyy-MM-dd HH:mm:ss z"): String =
    this.format(pattern = pattern, locale = Locale.getDefault())

/**
 * Formats this [ZonedDateTime] using a custom [pattern] and optional [Locale].
 *
 * ```kotlin
 * val zoned = ZonedDateTime.now(ZoneId.of("America/Mexico_City"))
 * println(zoned.format()) // "2026-01-05 14:30:00 -06:00[America/Mexico_City]"
 * println(zoned.format("dd/MM/yyyy HH:mm z", Locale("es","ES"))) // "05/01/2026 14:30 CST"
 * ```
 *
 * @receiver The [ZonedDateTime] to format.
 * @param pattern The date-time pattern, default is "yyyy-MM-dd HH:mm:ss z".
 * @param locale The [Locale] to use for formatting. Defaults to system default.
 * @return The formatted date-time string.
 * @since 2.2.6
 */
fun ZonedDateTime.format(pattern: String = "yyyy-MM-dd HH:mm:ss z", locale: Locale = Locale.getDefault()): String =
    this.format(DateTimeFormatter.ofPattern(pattern, locale))

/**
 * Formats this [ZonedDateTime] using localized date and time styles.
 * The [Locale] to use for formatting. Defaults to system default.
 *
 * ```kotlin
 * val zoned = ZonedDateTime.now(ZoneId.of("America/Mexico_City"))
 * println(zoned.formatDateTime()) // e.g., "01/05/26 14:30:00"
 * println(zoned.formatDateTime(FormatStyle.LONG, FormatStyle.SHORT, Locale("es","ES"))) // e.g., "5 de enero de 2026 14:30"
 * ```
 *
 * @receiver The [ZonedDateTime] to format.
 * @param dateStyle The localized date style. Default is [FormatStyle.SHORT].
 * @param timeStyle The localized time style. Default is [FormatStyle.MEDIUM].
 * @return The formatted date-time string.
 * @since 2.2.6
 */
fun ZonedDateTime.formatDateTime(dateStyle: FormatStyle = SHORT, timeStyle: FormatStyle = MEDIUM): String =
    this.formatDateTime(dateStyle = dateStyle, timeStyle = timeStyle, locale = Locale.getDefault())

/**
 * Formats this [ZonedDateTime] using localized date and time styles and optional [Locale].
 *
 * ```kotlin
 * val zoned = ZonedDateTime.now(ZoneId.of("America/Mexico_City"))
 * println(zoned.formatDateTime()) // e.g., "01/05/26 14:30:00"
 * println(zoned.formatDateTime(FormatStyle.LONG, FormatStyle.SHORT, Locale("es","ES"))) // e.g., "5 de enero de 2026 14:30"
 * ```
 *
 * @receiver The [ZonedDateTime] to format.
 * @param dateStyle The localized date style. Default is [FormatStyle.SHORT].
 * @param timeStyle The localized time style. Default is [FormatStyle.MEDIUM].
 * @param locale The [Locale] to use for formatting. Defaults to system default.
 * @return The formatted date-time string.
 * @since 2.2.6
 */
fun ZonedDateTime.formatDateTime(
    dateStyle: FormatStyle = SHORT,
    timeStyle: FormatStyle = MEDIUM,
    locale: Locale
): String =
    this.format(DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle).withLocale(locale))
