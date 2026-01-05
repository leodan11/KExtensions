package com.github.leodan11.k_extensions.calendar

import java.time.YearMonth

/**
 * Checks if this [YearMonth] is before another [YearMonth].
 *
 * ```kotlin
 * val jan2026 = YearMonth.of(2026, 1)
 * val feb2026 = YearMonth.of(2026, 2)
 * println(jan2026.isBeforeMonth(feb2026)) // true
 * ```
 *
 * @receiver The [YearMonth] to compare.
 * @param other The [YearMonth] to compare against.
 * @return `true` if this [YearMonth] is before the other.
 * @since 2.2.6
 */
fun YearMonth.isBeforeMonth(other: YearMonth?): Boolean = other?.let { this < it } ?: false

/**
 * Checks if this [YearMonth] is after another [YearMonth].
 *
 * ```kotlin
 * val jan2026 = YearMonth.of(2026, 1)
 * val feb2026 = YearMonth.of(2026, 2)
 * println(feb2026.isAfterMonth(jan2026)) // true
 * ```
 *
 * @receiver The [YearMonth] to compare.
 * @param other The [YearMonth] to compare against.
 * @return `true` if this [YearMonth] is after the other.
 * @since 2.2.6
 */
fun YearMonth.isAfterMonth(other: YearMonth): Boolean = this > other