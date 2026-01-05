package com.github.leodan11.k_extensions.calendar.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime

/**
 * Sealed class representing the possible Java Time results from parsing a string.
 * @since 2.2.6
 */
sealed class DateTimeResult {
    data class Date(val value: LocalDate) : DateTimeResult()
    data class Time(val value: LocalTime) : DateTimeResult()
    data class DateTime(val value: LocalDateTime) : DateTimeResult()
    data class OffsetDateTimeResult(val value: OffsetDateTime) : DateTimeResult()
}