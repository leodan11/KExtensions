package com.github.leodan11.k_extensions.calendar

import android.content.Context
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


/**
 *
 * @return An instance of the [Calendar] object with hour set to 00:00:00:00
 *
 */
val midnightCalendar: Calendar
    get() = Calendar.getInstance().apply {
        this.setMidnight()
    }


/**
 * This method sets an hour in the [Calendar] object to 00:00:00:00
 *
 * @receiver [Calendar] object which hour should be set to 00:00:00:00
 * @return An instance of the [Calendar]
 *
 */
fun Calendar.setMidnight() = this.apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}


/**
 * This method compares calendars using month and year
 *
 * @receiver [Calendar] object to compare
 * @param secondCalendar [Calendar] object to compare
 * @return [Boolean] value if second [Calendar] is before the first one
 *
 */
fun Calendar.isMonthBefore(secondCalendar: Calendar? = null): Boolean {
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
 * This method compares calendars using month and year
 *
 * @receiver [Calendar] object to compare
 * @param secondCalendar [Calendar] object to compare
 * @return [Boolean] value if second [Calendar] is after the first one
 *
 */
fun Calendar.isMonthAfter(secondCalendar: Calendar) = secondCalendar.isMonthBefore(this)


/**
 * This method returns a string containing a month's name and a year (in number).
 * It's used instead of new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date);
 * because that method returns a month's name in incorrect form in some languages (i.e. in Polish)
 *
 * @receiver [Calendar] object containing date which will be formatted
 * @param context  An array of months names
 * @return A [String] of the formatted date containing a month's name and a year (in number)
 *
 */
fun Calendar.getMonthAndYearDate(context: Context) = String.format(
    "%s  %s",
    context.resources.getStringArray(R.array.calendar_months_array_kt)[this.get(Calendar.MONTH)],
    this.get(Calendar.YEAR)
)


/**
 * This method is used to count a number of months between two dates
 *
 * @receiver [Calendar] representing a first date
 * @param endCalendar [Calendar] representing a last date
 * @return [Int] Number of months
 *
 */
fun Calendar.getMonthsToDate(endCalendar: Calendar): Int {
    val years = endCalendar.get(Calendar.YEAR) - this.get(Calendar.YEAR)
    return years * 12 + endCalendar.get(Calendar.MONTH) - this.get(Calendar.MONTH)
}


/**
 * This method checks whether date is correctly between min and max date or not
 *
 * @receiver [Calendar] object
 * @param minimumDate [Calendar] properties
 * @param maximumDate [Calendar] properties
 * @return [Boolean] value if date is between min and max date
 *
 */
fun Calendar.isBetweenMinAndMax(minimumDate: Calendar, maximumDate: Calendar) =
    !(this.before(minimumDate) || this.after(maximumDate))


/**
 * This method is used to count a number of days between two dates.  +1 is necessary because method counts from the beginning of start day to beginning of end day
 * and 1, means whole end day
 *
 *
 * @receiver [Calendar] representing a first date
 * @param endCalendar [Calendar] representing a last date
 * @return [Long] Number of days
 *
 */
fun Calendar.getDaysToDate(endCalendar: Calendar): Long {
    this.set(Calendar.DST_OFFSET, 0)
    endCalendar.set(Calendar.DST_OFFSET, 0)

    return TimeUnit.MILLISECONDS.toDays(endCalendar.timeInMillis - this.timeInMillis) + 1
}


/**
 * This method checks whether selected dates are full dates range
 *
 * @receiver [Calendar] object
 * @return [Boolean] value if selected dates are full dates range
 *
 */
fun List<Calendar>.isFullDatesRange(): Boolean {
    val selectedDates = this.distinct().sortedBy { it.timeInMillis }

    if (this.isEmpty() || selectedDates.size == 1) return true

    return selectedDates.size.toLong() == selectedDates.first().getDaysToDate(selectedDates.last())
}


/**
 * This method checks whether calendar is today
 *
 * @receiver [Calendar] object
 * @return [Boolean] value if [Calendar] is today
 *
 */
val Calendar.isToday get() = this == midnightCalendar


/**
 * This method checks whether two calendars are equal
 *
 * @receiver [Calendar] object
 * @param calendar [Calendar] object to compare
 * @return [Boolean] value if two calendars are equal
 *
 */
fun Calendar.isEqual(calendar: Calendar) = this.setMidnight() == calendar.setMidnight()


/**
 * This method returns a list of calendar objects between two dates
 *
 * @receiver [Calendar] representing a first selected date
 * @param toCalendar [Calendar] representing a last selected date
 * @return [List] of selected dates between two dates
 *
 */
fun Calendar.getDatesRange(toCalendar: Calendar): List<Calendar> =
    if (toCalendar.before(this)) {
        toCalendar.getCalendarsBetweenDates(this)
    } else {
        this.getCalendarsBetweenDates(toCalendar)
    }


/**
 * This method returns a list of calendar objects between two dates
 *
 * @receiver [Calendar] representing a first selected date
 * @param toCalendar [Calendar] representing a last selected date
 * @return [List] of selected dates between two dates
 *
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
 * This method returns a list of calendar objects between two dates
 *
 * @receiver [Calendar] object
 * @param dateFrom [Date] representing a first selected date
 * @param dateTo [Date] representing a last selected date
 * @return [List] of selected dates between two dates
 *
 */
fun getCalendarsBetweenDates(dateFrom: Date, dateTo: Date): List<Calendar> {
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


/**
 * Calendar to String format
 *
 * @receiver [Calendar] object
 * @param typeCast [DateFormat]. By default [DateFormat.SHORT]
 * @return [String] e.g. 7/4/01
 *
 */
fun Calendar.formatDate(typeCast: Int = DateFormat.SHORT): String =
    synchronized(this) { DateFormat.getDateInstance(typeCast).format(this.time) }


/**
 * Calendar to String format
 *
 * @receiver [Calendar] object
 * @param typeCastDate [DateFormat]. By default [DateFormat.SHORT]
 * @param typeCastTime [DateFormat]. By default [DateFormat.MEDIUM]
 * @return [String] a date/time formatter, e.g. 7/4/01, 3:53:52 PM
 *
 */
fun Calendar.formatDateTime(
    typeCastDate: Int = DateFormat.SHORT,
    typeCastTime: Int = DateFormat.MEDIUM,
): String = synchronized(this) {
    DateFormat.getDateTimeInstance(typeCastDate, typeCastTime).format(this.time)
}


/**
 * Calendar to String format
 *
 * @receiver [Calendar] object
 * @param typeCast [DateFormat]. By default [DateFormat.SHORT]
 * @return [String] e.g. 3:30 PM
 *
 */
fun Calendar.formatTime(typeCast: Int = DateFormat.SHORT): String = synchronized(this) {
    DateFormat.getTimeInstance(typeCast).format(this.time)
}


/**
 * Calendar to String format
 *
 * @receiver [Calendar] object
 * @param pattern [String]. By default yyyy-MM-dd
 * @return [String] e.g. 2001/07/04
 *
 */
fun Calendar.toFormat(pattern: String = "yyyy-MM-dd"): String = synchronized(this) {
    SimpleDateFormat(pattern, Locale.getDefault()).format(this.time)
}