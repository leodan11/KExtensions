package com.github.leodan11.k_extensions.core

import android.content.res.Resources
import android.util.TypedValue
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow


/**
 * Float to DP
 *
 * @return [Float]
 */
val Float.DP
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
val Float.SP
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
val Int.DP
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
val Int.SP
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