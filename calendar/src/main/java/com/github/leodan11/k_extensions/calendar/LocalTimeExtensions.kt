package com.github.leodan11.k_extensions.calendar

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale


/**
 * Returns the constant [LocalTime] representing midnight (00:00).
 *
 * This property is convenient when you need a reference to the start of the day.
 *
 * ```kotlin
 * val midnightTime = LocalTime.midnight
 * println(midnightTime) // 00:00
 * ```
 * @since 2.2.6
 */
val LocalTime.midnight: LocalTime
    get() = LocalTime.MIDNIGHT

/**
 * Formats this [LocalTime] using the specified [FormatStyle] and the system default locale.
 *
 * ```kotlin
 * val time = LocalTime.of(14, 30)
 * println(time.formatTime()) // e.g., 2:30 PM (depends on locale)
 * ```
 *
 * @param style The [FormatStyle] to apply. Defaults to [FormatStyle.SHORT].
 * @return A formatted time string.
 * @since 2.2.6
 */
fun LocalTime.formatTime(style: FormatStyle = FormatStyle.SHORT): String =
    this.formatTime(style = style, locale = Locale.getDefault())

/**
 * Formats this [LocalTime] using the specified [FormatStyle] and [Locale].
 *
 * ```kotlin
 * val time = LocalTime.of(14, 30)
 * println(time.formatTime(FormatStyle.MEDIUM, Locale("es", "ES"))) // e.g., 14:30:00
 * ```
 *
 * @param style The [FormatStyle] to apply. Defaults to [FormatStyle.SHORT].
 * @param locale The [Locale] to use for formatting.
 * @return A formatted time string.
 * @since 2.2.6
 */
fun LocalTime.formatTime(style: FormatStyle = FormatStyle.SHORT, locale: Locale): String =
    this.format(DateTimeFormatter.ofLocalizedTime(style).withLocale(locale))

/**
 * Formats this [LocalTime] using a custom [pattern] and the system default locale.
 *
 * ```kotlin
 * val time = LocalTime.of(14, 30, 15)
 * println(time.format("HH:mm:ss")) // 14:30:15
 * ```
 *
 * @param pattern The date-time pattern to use. Defaults to "HH:mm:ss".
 * @return A formatted time string.
 * @since 2.2.6
 */
fun LocalTime.format(pattern: String = "HH:mm:ss"): String =
    this.format(pattern = pattern, locale = Locale.getDefault())

/**
 * Formats this [LocalTime] using a custom [pattern] and specified [Locale].
 *
 * ```kotlin
 * val time = LocalTime.of(14, 30, 15)
 * println(time.format("HH:mm:ss", Locale("es", "ES"))) // 14:30:15
 * ```
 *
 * @param pattern The date-time pattern to use. Defaults to "HH:mm:ss".
 * @param locale The [Locale] to apply for formatting.
 * @return A formatted time string.
 * @since 2.2.6
 */
fun LocalTime.format(pattern: String = "HH:mm:ss", locale: Locale = Locale.getDefault()): String =
    this.format(DateTimeFormatter.ofPattern(pattern, locale))
