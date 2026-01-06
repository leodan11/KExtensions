package com.github.leodan11.k_extensions.calendar

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.FormatStyle.MEDIUM
import java.time.format.FormatStyle.SHORT
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

/**
 * Returns the current date-time at midnight (00:00) in the system default time zone.
 *
 * ```kotlin
 * val todayMidnight = LocalDateTime.todayAtMidnight
 * println(todayMidnight) // e.g., 2026-01-05T00:00
 * ```
 *
 * @return [LocalDateTime] representing today at 00:00.
 * @since 2.2.6
 */
val LocalDateTime.todayAtMidnight: LocalDateTime
    get() = LocalDate.now().atStartOfDay()

/**
 * Returns a [LocalDateTime] at midnight (00:00) of this date-time.
 *
 * ```kotlin
 * val dateTime = LocalDateTime.of(2026, 1, 5, 14, 30)
 * val midnight = dateTime.atMidnight()
 * println(midnight) // 2026-01-05T00:00
 * ```
 *
 * @receiver The [LocalDateTime] to convert to midnight.
 * @return A new [LocalDateTime] at 00:00 of the same date.
 * @since 2.2.6
 */
fun LocalDateTime.atMidnight(): LocalDateTime = this.toLocalDate().atStartOfDay()

/**
 * Checks if this [LocalDateTime] is before another date-time based on month and year.
 *
 * @receiver The [LocalDateTime] to compare.
 * @param other The other [LocalDateTime] to compare against.
 * @return `true` if this is before [other] by month and year, `false` otherwise.
 * @since 2.2.6
 */
fun LocalDateTime.isMonthBefore(other: LocalDateTime?): Boolean =
    other?.let { YearMonth.from(this) < YearMonth.from(it) } ?: false

/**
 * Checks if this [LocalDateTime] is after another date-time based on month and year.
 *
 * @receiver The [LocalDateTime] to compare.
 * @param other The other [LocalDateTime] to compare against.
 * @return `true` if this is after [other] by month and year, `false` otherwise.
 * @since 2.2.6
 */
fun LocalDateTime.isMonthAfter(other: LocalDateTime): Boolean =
    YearMonth.from(this) > YearMonth.from(other)

/**
 * Returns the localized name of the day of the week for this [LocalDateTime] using default locale.
 *
 * ```kotlin
 * val dt = LocalDateTime.of(2026, 1, 5, 14, 0)
 * println(dt.getDayName()) // e.g., "Monday"
 * ```
 *
 * @receiver The [LocalDateTime] to get the day name from.
 * @return Day name with first letter capitalized.
 * @since 2.2.6
 */
fun LocalDateTime.getDayName(): String = getDayName(Locale.getDefault())

/**
 * Returns the localized name of the day of the week for this [LocalDateTime] using [locale].
 *
 * ```kotlin
 * val dt = LocalDateTime.of(2026, 1, 5, 14, 0)
 * println(dt.getDayName(Locale("es", "ES"))) // "Lunes"
 * ```
 *
 * @receiver The [LocalDateTime] to get the day name from.
 * @param locale The locale to format the day name.
 * @return Day name with first letter capitalized.
 * @since 2.2.6
 */
fun LocalDateTime.getDayName(locale: Locale): String =
    this.dayOfWeek.getDisplayName(TextStyle.FULL, locale)
        .replaceFirstChar { it.uppercase(locale) }

/**
 * Returns the month name and year of this [LocalDateTime] in the format "MMMM yyyy". [Locale] for formatting. Defaults to system locale.
 *
 * ```kotlin
 * val dt = LocalDateTime.of(2026, 1, 5, 14, 0)
 * println(dt.getMonthAndYearDate()) // "January 2026"
 * ```
 *
 * @receiver The [LocalDateTime] to format.
 * @return A string in the format "MMMM yyyy".
 * @since 2.2.6
 */
fun LocalDateTime.getMonthAndYearDate(): String = this.getMonthAndYearDate(Locale.getDefault())


/**
 * Returns the month name and year of this [LocalDateTime] in the format "MMMM yyyy".
 *
 * ```kotlin
 * val dt = LocalDateTime.of(2026, 1, 5, 14, 0)
 * println(dt.getMonthAndYearDate()) // "January 2026"
 * ```
 *
 * @receiver The [LocalDateTime] to format.
 * @param locale Optional [Locale] for formatting. Defaults to system locale.
 * @return A string in the format "MMMM yyyy".
 * @since 2.2.6
 */
fun LocalDateTime.getMonthAndYearDate(locale: Locale): String =
    "${this.month.getDisplayName(TextStyle.FULL, locale)} ${this.year}"

/**
 * Formats this [LocalDateTime] to a string using a custom pattern.
 * The [Locale] to format. Defaults to system default.
 *
 * ```kotlin
 * val dt = LocalDateTime.of(2026, 1, 5, 14, 30)
 * println(dt.toFormat("yyyy/MM/dd HH:mm")) // "2026/01/05 14:30"
 * ```
 *
 * @receiver The [LocalDateTime] to format.
 * @param pattern The date-time pattern. Defaults to "yyyy-MM-dd".
 * @return Formatted date-time string.
 * @since 2.2.6
 */
fun LocalDateTime.toFormat(pattern: String = "yyyy-MM-dd"): String = this.toFormat(pattern = pattern, locale = Locale.getDefault())

/**
 * Formats this [LocalDateTime] to a string using a custom pattern and optional locale.
 *
 * ```kotlin
 * val dt = LocalDateTime.of(2026, 1, 5, 14, 30)
 * println(dt.toFormat("yyyy/MM/dd HH:mm")) // "2026/01/05 14:30"
 * ```
 *
 * @receiver The [LocalDateTime] to format.
 * @param pattern The date-time pattern. Defaults to "yyyy-MM-dd".
 * @param locale The [Locale] to format. Defaults to system default.
 * @return Formatted date-time string.
 * @since 2.2.6
 */
fun LocalDateTime.toFormat(pattern: String = "yyyy-MM-dd", locale: Locale): String =
    this.format(DateTimeFormatter.ofPattern(pattern, locale))

/**
 * Formats this [LocalDateTime] using pattern "yyyy-MM-dd HH:mm" by default.
 *
 * ```kotlin
 * val dt = LocalDateTime.of(2026, 1, 5, 14, 30)
 * println(dt.formatDateTime()) // "2026-01-05 14:30"
 * ```
 *
 * @receiver The [LocalDateTime] to format.
 * @param pattern The pattern to format the date-time string. Defaults to "yyyy-MM-dd HH:mm".
 * @return Formatted date-time string.
 * @since 2.2.6
 */
fun LocalDateTime.formatDateTime(pattern: String = "yyyy-MM-dd HH:mm"): String =
    this.format(DateTimeFormatter.ofPattern(pattern))

/**
 * Checks if this [LocalDateTime] is between [start] and [end], inclusive.
 *
 * @receiver The [LocalDateTime] to check.
 * @param start The start date-time.
 * @param end The end date-time.
 * @return `true` if within range, `false` otherwise.
 * @since 2.2.6
 */
fun LocalDateTime.isBetween(start: LocalDateTime, end: LocalDateTime): Boolean =
    !this.isBefore(start) && !this.isAfter(end)

/**
 * Returns the number of days from this [LocalDateTime] to [endDate], inclusive.
 *
 * ```kotlin
 * val start = LocalDateTime.of(2026, 1, 1, 0, 0)
 * val end = LocalDateTime.of(2026, 1, 5, 0, 0)
 * println(start.daysTo(end)) // 5
 * ```
 *
 * @receiver The start [LocalDateTime].
 * @param endDate The end [LocalDateTime].
 * @return Number of days including both start and end.
 * @since 2.2.6
 */
fun LocalDateTime.daysTo(endDate: LocalDateTime): Long = ChronoUnit.DAYS.between(this, endDate) + 1

/**
 * Formats this [LocalDateTime] using localized date and time styles.
 * The [Locale] to use for formatting. Defaults to system default.
 *
 * ```kotlin
 * val dateTime = LocalDateTime.of(2026, 1, 5, 14, 30)
 * println(dateTime.formatDateTime()) // e.g., "01/05/26 14:30:00"
 * println(dateTime.formatDateTime(FormatStyle.LONG, FormatStyle.SHORT)) // e.g., "January 5, 2026 14:30"
 * ```
 *
 * @receiver The [LocalDateTime] to format.
 * @param dateStyle The localized date style. Default is [FormatStyle.SHORT].
 * @param timeStyle The localized time style. Default is [FormatStyle.MEDIUM].
 * @return The formatted date-time string.
 * @since 2.2.6
 */
fun LocalDateTime.formatDateTime(dateStyle: FormatStyle = SHORT, timeStyle: FormatStyle = MEDIUM): String =
    this.formatDateTime(dateStyle = dateStyle, timeStyle = timeStyle, locale = Locale.getDefault())

/**
 * Formats this [LocalDateTime] using localized date and time styles and optional [Locale].
 *
 * ```kotlin
 * val dateTime = LocalDateTime.of(2026, 1, 5, 14, 30)
 * println(dateTime.formatDateTime()) // e.g., "01/05/26 14:30:00"
 * println(dateTime.formatDateTime(FormatStyle.LONG, FormatStyle.SHORT)) // e.g., "January 5, 2026 14:30"
 * ```
 *
 * @receiver The [LocalDateTime] to format.
 * @param dateStyle The localized date style. Default is [FormatStyle.SHORT].
 * @param timeStyle The localized time style. Default is [FormatStyle.MEDIUM].
 * @param locale The [Locale] to use for formatting. Defaults to system default.
 * @return The formatted date-time string.
 * @since 2.2.6
 */
fun LocalDateTime.formatDateTime(
    dateStyle: FormatStyle = SHORT,
    timeStyle: FormatStyle = MEDIUM,
    locale: Locale = Locale.getDefault()
): String =
    this.format(DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle).withLocale(locale))


/**
 * Formats this [LocalDateTime] using a custom [pattern].
 * The [Locale] to use for formatting. Defaults to system default.
 *
 * ```kotlin
 * val dateTime = LocalDateTime.of(2026, 1, 5, 14, 30, 15)
 * println(dateTime.format()) // "2026-01-05 14:30:15"
 * println(dateTime.format("dd/MM/yyyy HH:mm", Locale("es","ES"))) // "05/01/2026 14:30"
 * ```
 *
 * @receiver The [LocalDateTime] to format.
 * @param pattern The date-time pattern, default is "yyyy-MM-dd HH:mm:ss".
 * @return The formatted date-time string.
 * @since 2.2.6
 */
fun LocalDateTime.format(pattern: String = "yyyy-MM-dd HH:mm:ss"): String =
    this.format(pattern = pattern, locale = Locale.getDefault())

/**
 * Formats this [LocalDateTime] using a custom [pattern] and optional [Locale].
 *
 * ```kotlin
 * val dateTime = LocalDateTime.of(2026, 1, 5, 14, 30, 15)
 * println(dateTime.format()) // "2026-01-05 14:30:15"
 * println(dateTime.format("dd/MM/yyyy HH:mm", Locale("es","ES"))) // "05/01/2026 14:30"
 * ```
 *
 * @receiver The [LocalDateTime] to format.
 * @param pattern The date-time pattern, default is "yyyy-MM-dd HH:mm:ss".
 * @param locale The [Locale] to use for formatting. Defaults to system default.
 * @return The formatted date-time string.
 * @since 2.2.6
 */
fun LocalDateTime.format(pattern: String = "yyyy-MM-dd HH:mm:ss", locale: Locale): String =
    this.format(DateTimeFormatter.ofPattern(pattern, locale))
