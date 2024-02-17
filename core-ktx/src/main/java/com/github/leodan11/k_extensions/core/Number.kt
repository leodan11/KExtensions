package com.github.leodan11.k_extensions.core

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow

/**
 * Set the format of a double number
 *
 * @return [String] number converted to a set format, Default has [Locale.getDefault]
 */
fun Double.toNumberFormat(locale: Locale = Locale.getDefault()): String {
    return try {
        val contentValue = NumberFormat.getCurrencyInstance(locale)
        contentValue.format(this)
    }catch (e: Exception){
        e.printStackTrace()
        this.toString()
    }
}

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
 * Set the format of a double number
 *
 * @return [String] number converted to a set format, Default has [Locale.getDefault]
 */
fun Long.toNumberFormat(locale: Locale = Locale.getDefault()): String {
    return try {
        val contentValue = NumberFormat.getCurrencyInstance(locale)
        contentValue.format(this)
    }catch (e: Exception){
        e.printStackTrace()
        this.toString()
    }
}


/**
 * Convert a natural number to a real number with two decimals
 *
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
 * @return [Float]
 */
fun Number.to4decimalResFloat(locale: Locale = Locale.getDefault()): Float {
    val res = "%.4f".format(this, locale)
    val decimalFormatter = DecimalFormat("###,###,###,###.##")
    return decimalFormatter.parse(res)?.toFloat() ?: 0.0f
}