package com.github.leodan11.k_extensions.calendar

import android.content.Context
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


/**
 * Extension property that returns a [Calendar] instance set to midnight (00:00:00:000).
 *
 * @receiver The [Calendar] object.
 * @return A [Calendar] object with the time set to 00:00:00:000.
 */
val midnightCalendar: Calendar
    get() = Calendar.getInstance().apply {
        this.setMidnight()
    }

/**
 * Sets the time of the [Calendar] object to midnight (00:00:00:000).
 *
 * @receiver The [Calendar] object whose time should be set to midnight.
 * @return The [Calendar] object after being modified.
 */
fun Calendar.setMidnight(): Calendar = this.apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}

/**
 * Compares the current [Calendar] instance with another [Calendar] based on the month and year.
 *
 * @receiver The [Calendar] object to compare.
 * @param secondCalendar The [Calendar] object to compare against. If null, returns false.
 * @return `true` if the current [Calendar] is after the provided [secondCalendar] based on month and year.
 */
fun Calendar.isMonthBefore(secondCalendar: Calendar?): Boolean {
    if (secondCalendar == null) return false

    val firstDay = (this.clone() as Calendar).apply {
        setMidnight()
        set(Calendar.DAY_OF_MONTH, 1)
    }
    val secondDay = (secondCalendar.clone() as Calendar).apply {
        setMidnight()
        set(Calendar.DAY_OF_MONTH, 1)
    }

    return secondDay.before(firstDay)
}

/**
 * Compares the current [Calendar] instance with another [Calendar] based on the month and year.
 *
 * @receiver The [Calendar] object to compare.
 * @param secondCalendar The [Calendar] object to compare against.
 * @return `true` if the current [Calendar] is before the provided [secondCalendar] based on month and year.
 */
fun Calendar.isMonthAfter(secondCalendar: Calendar): Boolean = secondCalendar.isMonthBefore(this)

/**
 * Returns a string representing the month name and year in the format "MMMM yyyy".
 * This is used instead of `SimpleDateFormat("MMMM yyyy")` due to issues in certain languages (e.g., Polish).
 *
 * @receiver The [Calendar] object to format.
 * @param context The [Context] required to access string resources for month names.
 * @return A formatted string with the month name and year.
 * @throws IllegalArgumentException if [context] is null.
 */
fun Calendar.getMonthAndYearDate(context: Context): String {
    val monthNames = context.resources.getStringArray(R.array.calendar_months_array_kt)
    return String.format(
        "%s  %s",
        monthNames[this.get(Calendar.MONTH)],
        this.get(Calendar.YEAR)
    )
}

/**
 * Returns the number of months between the current [Calendar] and a given [endCalendar].
 *
 * @receiver The starting [Calendar] object.
 * @param endCalendar The ending [Calendar] object.
 * @return The number of months between the two [Calendar] objects.
 */
fun Calendar.getMonthsToDate(endCalendar: Calendar): Int {
    val years = endCalendar.get(Calendar.YEAR) - this.get(Calendar.YEAR)
    return years * 12 + endCalendar.get(Calendar.MONTH) - this.get(Calendar.MONTH)
}

/**
 * Checks if the current [Calendar] is between the given [minimumDate] and [maximumDate].
 *
 * @receiver The [Calendar] object to compare.
 * @param minimumDate The minimum allowable [Calendar] date.
 * @param maximumDate The maximum allowable [Calendar] date.
 * @return `true` if the current [Calendar] is between the [minimumDate] and [maximumDate].
 */
fun Calendar.isBetweenMinAndMax(minimumDate: Calendar, maximumDate: Calendar): Boolean =
    !(this.before(minimumDate) || this.after(maximumDate))

/**
 * Returns the number of days between the current [Calendar] and a given [endCalendar].
 * +1 is added to include the entire end day in the calculation.
 *
 * @receiver The starting [Calendar] object.
 * @param endCalendar The ending [Calendar] object.
 * @return The number of days between the two [Calendar] objects.
 * @throws IllegalArgumentException if [endCalendar] is earlier than the current [Calendar].
 */
fun Calendar.getDaysToDate(endCalendar: Calendar): Long {
    this.set(Calendar.DST_OFFSET, 0)
    endCalendar.set(Calendar.DST_OFFSET, 0)

    return TimeUnit.MILLISECONDS.toDays(endCalendar.timeInMillis - this.timeInMillis) + 1
}

/**
 * Checks if the list of [Calendar] objects represents a full date range.
 *
 * @receiver The list of [Calendar] objects to check.
 * @return `true` if the list represents a full date range (no gaps).
 */
fun List<Calendar>.isFullDatesRange(): Boolean {
    val selectedDates = this.distinct().sortedBy { it.timeInMillis }

    if (this.isEmpty() || selectedDates.size == 1) return true

    return selectedDates.size.toLong() == selectedDates.first().getDaysToDate(selectedDates.last())
}

/**
 * Checks if the [Calendar] object represents today's date.
 *
 * @receiver The [Calendar] object to check.
 * @return `true` if the [Calendar] represents today.
 */
val Calendar.isToday: Boolean
    get() = this == midnightCalendar

/**
 * Compares the current [Calendar] object with another [Calendar] to check if they are equal.
 *
 * @receiver The [Calendar] object to compare.
 * @param calendar The other [Calendar] object to compare with.
 * @return `true` if both [Calendar] objects represent the same moment in time, excluding time details.
 */
fun Calendar.isEqual(calendar: Calendar): Boolean = this.setMidnight() == calendar.setMidnight()

/**
 * Returns a list of [Calendar] objects between the current [Calendar] and another [Calendar].
 *
 * @receiver The starting [Calendar] object.
 * @param toCalendar The ending [Calendar] object.
 * @return A list of [Calendar] objects between the two given dates.
 * @throws IllegalArgumentException if the `toCalendar` is before the current [Calendar].
 */
fun Calendar.getDatesRange(toCalendar: Calendar): List<Calendar> =
    if (toCalendar.before(this)) {
        toCalendar.getCalendarsBetweenDates(this)
    } else {
        this.getCalendarsBetweenDates(toCalendar)
    }

/**
 * Returns a list of [Calendar] objects between the current [Calendar] and another [Calendar].
 *
 * @receiver The starting [Calendar] object.
 * @param toCalendar The ending [Calendar] object.
 * @return A list of [Calendar] objects between the two given dates.
 */
fun Calendar.getCalendarsBetweenDates(toCalendar: Calendar): List<Calendar> {
    val calendars = mutableListOf<Calendar>()

    val calendarFrom = this.apply {
        set(Calendar.DST_OFFSET, 0)
    }
    val calendarTo = toCalendar.apply {
        set(Calendar.DST_OFFSET, 0)
    }

    val daysBetweenDates = TimeUnit.MILLISECONDS.toDays(
        calendarTo.timeInMillis - calendarFrom.timeInMillis
    )

    (1 until daysBetweenDates).forEach {
        val calendar = calendarFrom.clone() as Calendar
        calendars.add(calendar)
        calendar.add(Calendar.DATE, it.toInt())
    }
    return calendars
}

/**
 * Formats the [Calendar] instance to a date string using the specified [typeCast] format.
 *
 * @receiver The [Calendar] instance to format.
 * @param typeCast The date format style (e.g., [DateFormat.SHORT], [DateFormat.MEDIUM], [DateFormat.LONG]). Default is [DateFormat.SHORT].
 * @return A formatted date string.
 */
fun Calendar.formatDate(typeCast: Int = DateFormat.SHORT): String =
    synchronized(this) { DateFormat.getDateInstance(typeCast).format(this.time) }

/**
 * Formats the [Calendar] instance to a date string using the specified [typeCast] and [locale].
 *
 * @receiver The [Calendar] instance to format.
 * @param typeCast The desired date format style.
 * @param locale The [Locale] to apply for formatting.
 * @return A localized formatted date string.
 */
fun Calendar.formatDate(typeCast: Int = DateFormat.SHORT, locale: Locale): String =
    synchronized(this) { DateFormat.getDateInstance(typeCast, locale).format(this.time) }

/**
 * Formats the [Calendar] instance to a date-time string using the given date and time format styles.
 *
 * @receiver The [Calendar] instance to format.
 * @param typeCastDate The date style (e.g., [DateFormat.SHORT]). Default is [DateFormat.SHORT].
 * @param typeCastTime The time style (e.g., [DateFormat.MEDIUM]). Default is [DateFormat.MEDIUM].
 * @return A formatted date-time string.
 */
fun Calendar.formatDateTime(
    typeCastDate: Int = DateFormat.SHORT,
    typeCastTime: Int = DateFormat.MEDIUM,
): String = synchronized(this) {
    DateFormat.getDateTimeInstance(typeCastDate, typeCastTime).format(this.time)
}

/**
 * Formats the [Calendar] instance to a localized date-time string using the specified [typeCastDate], [typeCastTime], and [locale].
 *
 * @receiver The [Calendar] instance to format.
 * @param typeCastDate The date style to use.
 * @param typeCastTime The time style to use.
 * @param locale The [Locale] to apply for formatting.
 * @return A localized formatted date-time string.
 */
fun Calendar.formatDateTime(
    typeCastDate: Int = DateFormat.SHORT,
    typeCastTime: Int = DateFormat.MEDIUM,
    locale: Locale
): String = synchronized(this) {
    DateFormat.getDateTimeInstance(typeCastDate, typeCastTime, locale).format(this.time)
}


/**
 * Converts the [Calendar] object to a string in time format using the provided format type.
 *
 * @receiver The [Calendar] object to format.
 * @param typeCast The desired time format type. Defaults to [DateFormat.SHORT].
 * @return The formatted time string, e.g., "3:30 PM".
 */
fun Calendar.formatTime(typeCast: Int = DateFormat.SHORT): String = synchronized(this) {
    DateFormat.getTimeInstance(typeCast).format(this.time)
}

/**
 * Formats the [Calendar] instance to a localized time string using the specified [typeCast] and [locale].
 *
 * @receiver The [Calendar] instance to format.
 * @param typeCast The time style to use.
 * @param locale The [Locale] to apply for formatting.
 * @return A localized formatted time string.
 */
fun Calendar.formatTime(typeCast: Int = DateFormat.SHORT, locale: Locale): String =
    synchronized(this) {
        DateFormat.getTimeInstance(typeCast, locale).format(this.time)
    }

/**
 * Converts the [Calendar] object to a string using the provided pattern.
 *
 * @receiver The [Calendar] object to format.
 * @param pattern The date pattern to use for formatting. Defaults to "yyyy-MM-dd".
 * @return The formatted date string, e.g., "2001/07/04".
 */
fun Calendar.toFormat(pattern: String = "yyyy-MM-dd"): String = synchronized(this) {
    SimpleDateFormat(pattern, Locale.getDefault()).format(this.time)
}

/**
 * Formats the [Calendar] instance using the provided [pattern] and [locale].
 *
 * @receiver The [Calendar] to be formatted.
 * @param pattern The desired date pattern.
 * @param locale The [Locale] used for formatting.
 * @return A localized formatted string.
 */
fun Calendar.toFormat(pattern: String = "yyyy-MM-dd", locale: Locale): String = synchronized(this) {
    SimpleDateFormat(pattern, locale).format(this.time)
}

/**
 * Returns a list of [Calendar] objects between two given [Date] objects.
 *
 * @param dateTo The ending [Date].
 * @return A list of [Calendar] objects between the two given dates.
 */
fun Date.getCalendarsBetweenDates(dateTo: Date): List<Calendar> {
    return onGetCalendarsBetweenDates(this, dateTo)
}

/**
 * Converts the [Date] object to a string using the provided date format type.
 *
 * @receiver The [Date] object to format.
 * @param typeCast The desired date format type. Defaults to [DateFormat.SHORT].
 * @return The formatted date string.
 */
fun Date.formatDate(typeCast: Int = DateFormat.SHORT): String =
    synchronized(this) { DateFormat.getDateInstance(typeCast).format(this) }

/**
 * Formats the [Date] instance using the specified date style and [locale].
 *
 * @receiver The [Date] to format.
 * @param typeCast The desired format style.
 * @param locale The [Locale] used for formatting.
 * @return A localized formatted date string.
 */
fun Date.formatDate(typeCast: Int = DateFormat.SHORT, locale: Locale): String =
    synchronized(this) { DateFormat.getDateInstance(typeCast, locale).format(this) }

/**
 * Converts the [Date] object to a string in both date and time formats using the provided format types.
 *
 * @receiver The [Date] object to format.
 * @param typeCastDate The desired date format type. Defaults to [DateFormat.SHORT].
 * @param typeCastTime The desired time format type. Defaults to [DateFormat.MEDIUM].
 * @return The formatted date-time string, e.g., "7/4/01, 3:53:52 PM".
 */
fun Date.formatDateTime(
    typeCastDate: Int = DateFormat.SHORT,
    typeCastTime: Int = DateFormat.MEDIUM,
): String = synchronized(this) {
    DateFormat.getDateTimeInstance(typeCastDate, typeCastTime).format(this)
}

/**
 * Formats the [Date] instance to a localized date-time string using the given styles and [locale].
 *
 * @receiver The [Date] to format.
 * @param typeCastDate The desired date style.
 * @param typeCastTime The desired time style.
 * @param locale The [Locale] used for formatting.
 * @return A localized formatted date-time string.
 */
fun Date.formatDateTime(
    typeCastDate: Int = DateFormat.SHORT,
    typeCastTime: Int = DateFormat.MEDIUM,
    locale: Locale
): String = synchronized(this) {
    DateFormat.getDateTimeInstance(typeCastDate, typeCastTime, locale).format(this)
}

/**
 * Converts the [Date] object to a string in time format using the provided format type.
 *
 * @receiver The [Date] object to format.
 * @param typeCast The desired time format type. Defaults to [DateFormat.SHORT].
 * @return The formatted time string, e.g., "3:30 PM".
 */
fun Date.formatTime(typeCast: Int = DateFormat.SHORT): String = synchronized(this) {
    DateFormat.getTimeInstance(typeCast).format(this)
}

/**
 * Formats the [Date] instance to a localized time string using the given [typeCast] and [locale].
 *
 * @receiver The [Date] to format.
 * @param typeCast The desired time style.
 * @param locale The [Locale] used for formatting.
 * @return A localized formatted time string.
 */
fun Date.formatTime(typeCast: Int = DateFormat.SHORT, locale: Locale): String = synchronized(this) {
    DateFormat.getTimeInstance(typeCast, locale).format(this)
}

/**
 * Converts the [Date] object to a string using the provided pattern.
 *
 * @receiver The [Date] object to format.
 * @param pattern The date pattern to use for formatting. Defaults to "yyyy-MM-dd".
 * @return The formatted date string, e.g., "2001/07/04".
 */
fun Date.toFormat(pattern: String = "yyyy-MM-dd"): String = synchronized(this) {
    SimpleDateFormat(pattern, Locale.getDefault()).format(this)
}

/**
 * Formats this [Date] into a [String] based on the specified [pattern] and [locale].
 *
 * This function uses [SimpleDateFormat] internally, so the pattern must conform to
 * Java date/time format conventions (e.g., `"yyyy-MM-dd"` or `"dd MMMM yyyy"`).
 *
 * @receiver The [Date] instance to format.
 * @param pattern The pattern describing the date and time format. Default is `"yyyy-MM-dd"`.
 * @param locale The [Locale] to apply during formatting (e.g., [Locale.US], [Locale.getDefault()]).
 *
 * @return A string representation of the date formatted according to the specified [pattern] and [locale].
 *
 * @see java.text.SimpleDateFormat
 * @sample java.util.Date().toFormat("dd/MM/yyyy", Locale.US)
 *
 * @throws IllegalArgumentException If the [pattern] is invalid.
 */
fun Date.toFormat(pattern: String = "yyyy-MM-dd", locale: Locale): String = synchronized(this) {
    SimpleDateFormat(pattern, locale).format(this)
}

/**
 * Returns a list of [Calendar] objects between two given [Date] objects.
 *
 * @param dateFrom The starting [Date].
 * @param dateTo The ending [Date].
 * @return A list of [Calendar] objects between the two given dates.
 */
private fun onGetCalendarsBetweenDates(dateFrom: Date, dateTo: Date): List<Calendar> {
    val calendars = mutableListOf<Calendar>()

    val calendarFrom = Calendar.getInstance().apply {
        time = dateFrom
        set(Calendar.DST_OFFSET, 0)
    }
    val calendarTo = Calendar.getInstance().apply {
        time = dateTo
        set(Calendar.DST_OFFSET, 0)
    }

    val daysBetweenDates = TimeUnit.MILLISECONDS.toDays(
        calendarTo.timeInMillis - calendarFrom.timeInMillis
    )

    (1 until daysBetweenDates).forEach {
        val calendar = calendarFrom.clone() as Calendar
        calendars.add(calendar)
        calendar.add(Calendar.DATE, it.toInt())
    }
    return calendars
}