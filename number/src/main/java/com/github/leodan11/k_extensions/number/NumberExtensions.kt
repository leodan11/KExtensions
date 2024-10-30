package com.github.leodan11.k_extensions.number

import android.content.res.Resources
import android.util.TypedValue
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow


/**
 * Float to DP
 *
 * @return [Float]
 */
val Float.asDp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )


/**
 * Float to SP
 *
 * @return [Float]
 */
val Float.asSp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        Resources.getSystem().displayMetrics
    )


/**
 * Int to DP
 *
 * @return [Int]
 */
val Int.asDp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()


/**
 * Int to SP
 *
 * @return [Int]
 */
val Int.asSp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )


/**
 * Convert numbers to human-readable format.
 *
 * @return [String]  e.g: 1024 ~ 1K
 */
val Long.formatHumanReadable: String
    get() = log10(coerceAtLeast(1).toDouble()).toInt().div(3).let {
        val precision = when (it) {
            0 -> 0; else -> 1
        }
        val suffix = arrayOf("", "K", "M", "G", "T", "P", "E", "Z", "Y")
        String.format("%.${precision}f ${suffix[it]}", toDouble() / 10.0.pow(it * 3))
    }


/**
 * Convert a natural number to a real number with two decimals
 *
 * @param locale [Locale] Default has [Locale.getDefault]
 * @return [String]
 */
fun Number.to2Decimal(locale: Locale = Locale.getDefault()): String {
    val decimalFormatSymbol = DecimalFormatSymbols(locale)
    decimalFormatSymbol.decimalSeparator = '.'
    decimalFormatSymbol.groupingSeparator = ','
    val decimalFormatter = DecimalFormat("###,###,###,###.##", decimalFormatSymbol)
    decimalFormatter.minimumFractionDigits = 2
    return decimalFormatter.format(this)
}


/**
 * Convert a natural number to a real number with three decimals
 *
 * @param locale [Locale] Default has [Locale.getDefault]
 * @return [String]
 */
fun Number.to3Decimal(locale: Locale = Locale.getDefault()): String {
    val decimalFormatSymbol = DecimalFormatSymbols(locale)
    decimalFormatSymbol.decimalSeparator = '.'
    decimalFormatSymbol.groupingSeparator = ','
    val decimalFormatter = DecimalFormat("###,###,###,###.####", decimalFormatSymbol)
    decimalFormatter.minimumFractionDigits = 3
    return decimalFormatter.format(this)
}


/**
 * Convert a natural number to a real number with four decimals
 *
 * @param locale [Locale] Default has [Locale.getDefault]
 * @return [String]
 */
fun Number.to4Decimal(locale: Locale = Locale.getDefault()): String {
    val decimalFormatSymbol = DecimalFormatSymbols(locale)
    decimalFormatSymbol.decimalSeparator = '.'
    decimalFormatSymbol.groupingSeparator = ','
    val decimalFormatter = DecimalFormat("###,###,###,###.####", decimalFormatSymbol)
    decimalFormatter.minimumFractionDigits = 4
    return decimalFormatter.format(this)
}


/**
 * Convert a natural number to a real number with two decimals
 *
 * @param locale [Locale] Default has [Locale.getDefault]
 * @return [Float]
 */
fun Number.to2decimalResFloat(locale: Locale = Locale.getDefault()): Float {
    val res = "%.2f".format(this, locale)
    val decimalFormatter = DecimalFormat("###,###,###,###.####")
    return decimalFormatter.parse(res)?.toFloat() ?: 0.0f
}


/**
 * Convert a natural number to a real number with three decimals
 *
 * @param locale [Locale] Default has [Locale.getDefault]
 * @return [Float]
 */
fun Number.to3decimalResFloat(locale: Locale = Locale.getDefault()): Float {
    val res = "%.3f".format(this, locale)
    val decimalFormatter = DecimalFormat("###,###,###,###.##")
    return decimalFormatter.parse(res)?.toFloat() ?: 0.0f
}


/**
 * Convert a natural number to a real number with four decimals
 *
 * @param locale [Locale] Default has [Locale.getDefault]
 * @return [Float]
 */
fun Number.to4decimalResFloat(locale: Locale = Locale.getDefault()): Float {
    val res = "%.4f".format(this, locale)
    val decimalFormatter = DecimalFormat("###,###,###,###.##")
    return decimalFormatter.parse(res)?.toFloat() ?: 0.0f
}


/**
 * Set the format of a number.
 *
 * @param locale [Locale] Default has [Locale.getDefault]
 * @return [String]
 */
fun Number.toNumberFormat(locale: Locale = Locale.getDefault()): String {
    return try {
        val contentValue = NumberFormat.getNumberInstance(locale)
        contentValue.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
        this.toString()
    }
}


/**
 * Set the format of a number.
 *
 * @param locale [Locale] Default has [Locale.getDefault]
 * @return [String] e.g: 250 to $250.00
 */
fun Number.toNumberFormatCurrency(locale: Locale = Locale.getDefault()): String {
    return try {
        val contentValue = NumberFormat.getCurrencyInstance(locale)
        contentValue.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
        this.toString()
    }
}


/**
 * Set the format of a number.
 *
 * @param locale [Locale] Default has [Locale.getDefault]
 * @return [String] e.g: 250.10 to 250
 */
fun Number.toNumberFormatInt(locale: Locale = Locale.getDefault()): String {
    return try {
        val contentValue = NumberFormat.getIntegerInstance(locale)
        contentValue.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
        this.toString()
    }
}


/**
 * Set the format of a number.
 *
 * @param locale [Locale] Default has [Locale.getDefault]
 * @return [String] e.g: 250 to 250.000 %
 */
fun Number.toNumberFormatPercent(locale: Locale = Locale.getDefault()): String {
    return try {
        val contentValue = NumberFormat.getPercentInstance(locale)
        contentValue.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
        this.toString()
    }
}


/**
 * Convert Celsius temperature to Fahrenheit
 */
fun Double.celsiusToFahrenheit(): Double = (this * 1.8) + 32


/**
 * Convert Fahrenheit temperature to Celsius
 */
fun Double.fahrenheitToCelsius(): Double = (this - 32) * 5 / 9


/**
 * Convert meters to miles
 */
val Double.metersToMiles: Double
    get() {
        return if (this != 0.0) {
            this / 1609.344
        } else -1.0
    }


/**
 * Converts meters to km
 */
val Double.metersToKM get() = this / 1000


/**
 * Returns kilometers to meters
 */
val Double.kilometersToMeters get() = this * 1000


/**
 * Convert miles to meters
 */
val Double.milesToMeters: Double
    get() {
        return if (this != 0.0) {
            this * 1609.344
        } else -1.0
    }


/**
 * Converts given bytes to human readable form.
 */
fun Long.convertBytesToHumanReadableForm(
    si: Boolean = false,
    locale: Locale = Locale.getDefault()
): String {
    val unit = if (si) 1000 else 1024
    if (this < unit) return toString() + " B"
    val exp = (ln(toDouble()) / ln(unit.toDouble())).toInt()
    val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else "i"
    return String.format(locale, "%.1f %sB", this / unit.toDouble().pow(exp.toDouble()), pre)
}

