package com.github.leodan11.k_extensions.calendar

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.FormatStyle.SHORT
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

/* ---------- TODAY ---------- */

/**
 * Returns the current date as a [LocalDate].
 *
 * ```kotlin
 * val today = LocalDate.today()
 * println(today) // e.g., 2026-01-05
 * ```
 *
 * @return [LocalDate] representing today's date.
 * @since 2.2.6
 */
fun LocalDate.today(): LocalDate = LocalDate.now()


/* ---------- MIDNIGHT ---------- */

/**
 * Returns the same [LocalDate] (already representing midnight by default).
 *
 * @receiver The [LocalDate] instance.
 * @return The same [LocalDate] instance.
 * @since 2.2.6
 */
fun LocalDate.atMidnight(): LocalDate = this


/* ---------- MONTH COMPARISON ---------- */

/**
 * Checks if this [LocalDate] is before another date based on month and year.
 *
 * @receiver The [LocalDate] to compare.
 * @param other The [LocalDate] to compare against.
 *
 *
 * ```kotlin
 * val date1 = LocalDate.of(2026, 1, 15)
 * val date2 = LocalDate.of(2026, 2, 1)
 * println(date1.isMonthBefore(date2)) // true
 * ```
 * @return `true` if this date is before the other based on month and year.
 * @since 2.2.6
 */
fun LocalDate.isMonthBefore(other: LocalDate?): Boolean = other?.let {
    YearMonth.from(this) < YearMonth.from(it)
} ?: false

/**
 * Checks if this [LocalDate] is after another date based on month and year.
 *
 * @receiver The [LocalDate] to compare.
 * @param other The [LocalDate] to compare against.
 *
 *
 * ```kotlin
 * val date1 = LocalDate.of(2026, 1, 15)
 * val date2 = LocalDate.of(2026, 2, 1)
 * println(date2.isMonthAfter(date1)) // true
 * ```
 * @return `true` if this date is after the other based on month and year.
 * @since 2.2.6
 */
fun LocalDate.isMonthAfter(other: LocalDate): Boolean = YearMonth.from(this) > YearMonth.from(other)


/* ---------- DAY / MONTH NAMES ---------- */

/**
 * Returns the localized name of the day of the week for this [LocalDate] instance,
 * using the default locale.
 *
 * @receiver The [LocalDate] to get the day name from.
 * @return The day name with its first letter capitalized.
 * @since 2.2.6
 */
fun LocalDate.getDayName(): String = getDayName(Locale.getDefault())

/**
 * Returns the localized name of the day of the week for this [LocalDate] instance,
 * using the specified [locale].
 *
 * @receiver The [LocalDate] to get the day name from.
 * @param locale The locale to use for formatting.
 *
 *
 * ```kotlin
 * val date = LocalDate.of(2026, 1, 5)
 * println(date.getDayName(Locale("es", "ES"))) // "Lunes"
 * ```
 * @return The day name with its first letter capitalized.
 * @since 2.2.6
 */
fun LocalDate.getDayName(locale: Locale): String {
    return this.dayOfWeek.getDisplayName(TextStyle.FULL, locale)
        .replaceFirstChar { it.uppercase(locale) }
}

/**
 * Returns the month name and year in the format "MMMM yyyy" for this [LocalDate]. The [Locale] to format the month name. Defaults to system default.
 *
 * @receiver The [LocalDate] object.
 *
 *
 * ```kotlin
 * val date = LocalDate.of(2026, 1, 5)
 * println(date.getMonthAndYearDate()) // "January 2026"
 * ```
 * @return A string in the format "MMMM yyyy".
 * @since 2.2.6
 */
fun LocalDate.getMonthAndYearDate(): String {
    return this.getMonthAndYearDate(locale = Locale.getDefault())
}

/**
 * Returns the month name and year in the format "MMMM yyyy" for this [LocalDate].
 *
 * @receiver The [LocalDate] object.
 * @param locale The [Locale] to format the month name.
 *
 *
 * ```kotlin
 * val date = LocalDate.of(2026, 1, 5)
 * println(date.getMonthAndYearDate()) // "January 2026"
 * ```
 * @return A string in the format "MMMM yyyy".
 * @since 2.2.6
 */
fun LocalDate.getMonthAndYearDate(locale: Locale): String {
    return "${this.month.getDisplayName(TextStyle.FULL, locale)} ${this.year}"
}


/* ---------- RANGES / DIFFERENCES ---------- */

/**
 * Returns the number of months between this [LocalDate] and [end].
 *
 * @receiver The start [LocalDate].
 * @param end The end [LocalDate].
 * @return Number of months difference.
 * @since 2.2.6
 */
fun LocalDate.monthsTo(end: LocalDate): Int =
    ChronoUnit.MONTHS.between(this.withDayOfMonth(1), end.withDayOfMonth(1)).toInt()

/**
 * Checks if this [LocalDate] is between [min] and [max], inclusive.
 *
 * @receiver The [LocalDate] to check.
 * @param min Minimum date.
 * @param max Maximum date.
 * @return `true` if this date is between min and max.
 * @since 2.2.6
 */
fun LocalDate.isBetween(min: LocalDate, max: LocalDate): Boolean = !this.isBefore(min) && !this.isAfter(max)

/**
 * Returns the number of days to [end], inclusive.
 *
 * @receiver The start [LocalDate].
 * @param end The end [LocalDate].
 * @return Number of days difference, including the end date.
 * @since 2.2.6
 */
fun LocalDate.daysTo(end: LocalDate): Long = ChronoUnit.DAYS.between(this, end) + 1

/**
 * Checks if the list of [LocalDate] represents a full consecutive range.
 *
 * @receiver List of [LocalDate].
 * @return `true` if dates are consecutive and complete.
 * @since 2.2.6
 */
fun List<LocalDate>.isFullRange(): Boolean {
    if (this.isEmpty() || this.size == 1) return true
    val sorted = this.distinct().sorted()
    return ChronoUnit.DAYS.between(sorted.first(), sorted.last()) + 1 == sorted.size.toLong()
}

/**
 * Checks if this [LocalDate] represents today.
 *
 * @receiver The [LocalDate] to check.
 * @return `true` if this date is today.
 * @since 2.2.6
 */
val LocalDate.isToday: Boolean get() = this == LocalDate.now()

/**
 * Checks if this [LocalDate] equals [other].
 *
 * @receiver The [LocalDate] to compare.
 * @param other The [LocalDate] to compare.
 * @return `true` if dates are equal.
 * @since 2.2.6
 */
fun LocalDate.isSameAs(other: LocalDate): Boolean = this == other

/**
 * Returns a list of [LocalDate] from this date to [to], inclusive.
 *
 * @receiver The start [LocalDate].
 * @param to The end [LocalDate].
 * @return List of consecutive [LocalDate] from start to end.
 * @since 2.2.6
 */
fun LocalDate.toListDatesUntil(to: LocalDate): List<LocalDate> {
    val days = ChronoUnit.DAYS.between(this, to).toInt()
    return (0..days).map { this.plusDays(it.toLong()) }
}

/**
 * Formats this [LocalDate] using a localized [FormatStyle] and the [Locale] to use for formatting. Defaults to system default.
 *
 * ```kotlin
 * val date = LocalDate.of(2026, 1, 5)
 * println(date.formatDate()) // e.g., "01/05/26" depending on locale
 * println(date.formatDate(FormatStyle.LONG)) // e.g., "January 5, 2026"
 * ```
 *
 * @receiver The [LocalDate] to format.
 * @param style The localized date style. Default is [FormatStyle.SHORT].
 * @return The formatted date string.
 * @since 2.2.6
 */
fun LocalDate.formatDate(style: FormatStyle = SHORT): String =
    this.formatDate(style = style, locale = Locale.getDefault() )

/**
 * Formats this [LocalDate] using a localized [FormatStyle] and [Locale].
 *
 * ```kotlin
 * val date = LocalDate.of(2026, 1, 5)
 * println(date.formatDate()) // e.g., "01/05/26" depending on locale
 * println(date.formatDate(FormatStyle.LONG)) // e.g., "January 5, 2026"
 * ```
 *
 * @receiver The [LocalDate] to format.
 * @param style The localized date style. Default is [FormatStyle.SHORT].
 * @param locale The [Locale] to use for formatting.
 * @return The formatted date string.
 * @since 2.2.6
 */
fun LocalDate.formatDate(style: FormatStyle = SHORT, locale: Locale): String =
    this.format(DateTimeFormatter.ofLocalizedDate(style).withLocale(locale))

/**
 * Formats this [LocalDate] using a custom [pattern] and The [Locale] to use for formatting. Defaults to system default.
 *
 * ```kotlin
 * val date = LocalDate.of(2026, 1, 5)
 * println(date.format("dd/MM/yyyy")) // "05/01/2026"
 * println(date.format("MMMM yyyy", Locale("es","ES"))) // "enero 2026"
 * ```
 *
 * @receiver The [LocalDate] to format.
 * @param pattern The date pattern, default is "yyyy-MM-dd".
 * @return The formatted date string.
 * @since 2.2.6
 */
fun LocalDate.format(pattern: String = "yyyy-MM-dd"): String =
    this.format(pattern = pattern, locale = Locale.getDefault())

/**
 * Formats this [LocalDate] using a custom [pattern] and optional [Locale].
 *
 * ```kotlin
 * val date = LocalDate.of(2026, 1, 5)
 * println(date.format("dd/MM/yyyy")) // "05/01/2026"
 * println(date.format("MMMM yyyy", Locale("es","ES"))) // "enero 2026"
 * ```
 *
 * @receiver The [LocalDate] to format.
 * @param pattern The date pattern, default is "yyyy-MM-dd".
 * @param locale The [Locale] to use for formatting. Defaults to system default.
 * @return The formatted date string.
 * @since 2.2.6
 */
fun LocalDate.format(pattern: String = "yyyy-MM-dd", locale: Locale = Locale.getDefault()): String =
    this.format(DateTimeFormatter.ofPattern(pattern, locale))
