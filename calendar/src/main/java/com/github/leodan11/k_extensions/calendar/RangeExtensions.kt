package com.github.leodan11.k_extensions.calendar

import java.time.LocalDate

/**
 * Returns a list of [LocalDate] between two dates, inclusive.
 * @since 2.2.6
 */
fun LocalDate.rangeTo(endDate: LocalDate): List<LocalDate> {
    require(!endDate.isBefore(this)) { "End date must be after start date" }
    return generateSequence(this) { it.plusDays(1) }.takeWhile { !it.isAfter(endDate) }.toList()
}

/**
 * Checks if a list of [LocalDate] represents a full consecutive date range.
 * @since 2.2.6
 */
fun List<LocalDate>.isFullDateRange(): Boolean {
    if (isEmpty() || size == 1) return true
    val sorted = distinct().sorted()
    return sorted.zipWithNext().all { it.first.plusDays(1) == it.second }
}