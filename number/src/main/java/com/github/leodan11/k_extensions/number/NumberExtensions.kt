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
 * Formats the number to a string with two decimal places using the default system locale.
 *
 * This function delegates to [to2Decimal] with the default locale.
 *
 * @return A string representing the number formatted with two decimals and a thousand separators.
 */
fun Number.to2Decimal(): String {
    return this.to2Decimal(Locale.getDefault())
}

/**
 * Formats the number to a string with two decimal places using the specified locale.
 *
 * Applies a numeric format with at least two decimal digits and
 * uses '.' as decimal separator and ',' as grouping separator,
 * regardless of locale for consistency.
 *
 * @param locale The locale used to obtain formatting symbols (e.g., grouping separator).
 * @return A string representing the number formatted with two decimals and a thousand separators.
 *
 * @throws IllegalArgumentException if [locale] is null.
 */
fun Number.to2Decimal(locale: Locale): String {

    val decimalFormatSymbols = DecimalFormatSymbols(locale).apply {
        decimalSeparator = '.'
        groupingSeparator = ','
    }
    val decimalFormatter = DecimalFormat("###,###,###,###.##", decimalFormatSymbols).apply {
        minimumFractionDigits = 2
    }
    return decimalFormatter.format(this)
}


/**
 * Formats the number to a string with three decimal places using the default system locale.
 *
 * This function delegates to [to3Decimal] with the default locale.
 *
 * @return A string representing the number formatted with three decimals and a thousand separators.
 */
fun Number.to3Decimal(): String {
    return this.to3Decimal(Locale.getDefault())
}

/**
 * Formats the number to a string with three decimal places using the specified locale.
 *
 * Uses '.' as decimal separator and ',' as grouping separator to maintain consistency,
 * regardless of locale's usual separators.
 *
 * @param locale The locale to retrieve formatting symbols for grouping separator.
 * @return A string representing the number formatted with three decimals and a thousand separators.
 *
 * @throws IllegalArgumentException if [locale] is null.
 */
fun Number.to3Decimal(locale: Locale): String {

    val decimalFormatSymbols = DecimalFormatSymbols(locale).apply {
        decimalSeparator = '.'
        groupingSeparator = ','
    }
    val decimalFormatter = DecimalFormat("###,###,###,###.####", decimalFormatSymbols).apply {
        minimumFractionDigits = 3
    }
    return decimalFormatter.format(this)
}


/**
 * Formats the number to a string with four decimal places using the default system locale.
 *
 * Delegates to [to4Decimal] specifying the default locale.
 *
 * @return A string representing the number formatted with four decimals and a thousand separators.
 */
fun Number.to4Decimal(): String {
    return this.to4Decimal(Locale.getDefault())
}

/**
 * Formats the number to a string with four decimal places using the specified locale.
 *
 * This function forces '.' as the decimal separator and ',' as the grouping separator to
 * maintain a consistent output format regardless of the locale's default separators.
 *
 * @param locale The locale for formatting symbols.
 * @return A string representation of the number with four decimal places and a thousand separators.
 *
 * @throws IllegalArgumentException if [locale] is null.
 */
fun Number.to4Decimal(locale: Locale): String {

    val decimalFormatSymbols = DecimalFormatSymbols(locale).apply {
        decimalSeparator = '.'
        groupingSeparator = ','
    }
    val decimalFormatter = DecimalFormat("###,###,###,###.####", decimalFormatSymbols).apply {
        minimumFractionDigits = 4
    }
    return decimalFormatter.format(this)
}


/**
 * Converts the number to a [Float] rounded to two decimal places
 * using the system's default locale.
 *
 * Delegates to [to2decimalResFloat] with the default locale.
 *
 * @return The float value rounded to two decimals.
 */
fun Number.to2decimalResFloat(): Float {
    return this.to2decimalResFloat(Locale.getDefault())
}

/**
 * Converts the number to a [Float] rounded to two decimal places
 * using the specified [locale].
 *
 * The number is first formatted to a string with two decimals,
 * then parsed back to a float to ensure proper rounding.
 *
 * @param locale The locale used for formatting (must not be null).
 * @return The float value rounded to two decimals.
 *
 * @throws IllegalArgumentException if [locale] is null.
 */
fun Number.to2decimalResFloat(locale: Locale): Float {

    val formatted = "%.2f".format(locale, this)
    val decimalFormatter = DecimalFormat("###,###,###,###.####")
    return decimalFormatter.parse(formatted)?.toFloat() ?: 0.0f
}


/**
 * Converts the number to a [Float] rounded to three decimal places
 * using the system's default locale.
 *
 * Delegates to [to3decimalResFloat] with the default locale.
 *
 * @return The float value rounded to three decimals.
 */
fun Number.to3decimalResFloat(): Float {
    return this.to3decimalResFloat(Locale.getDefault())
}

/**
 * Converts the number to a [Float] rounded to three decimal places
 * using the specified [locale].
 *
 * The number is first formatted as a string with three decimals,
 * then parsed back to a float to ensure proper rounding.
 *
 * @param locale The locale used for formatting (must not be null).
 * @return The float value rounded to three decimals.
 *
 * @throws IllegalArgumentException if [locale] is null.
 */
fun Number.to3decimalResFloat(locale: Locale): Float {

    val formatted = "%.3f".format(locale, this)
    val decimalFormatter = DecimalFormat("###,###,###,###.##")
    return decimalFormatter.parse(formatted)?.toFloat() ?: 0.0f
}


/**
 * Converts the number to a [Float] rounded to four decimal places
 * using the system's default locale.
 *
 * Delegates to [to4decimalResFloat] with the default locale.
 *
 * @return The float value rounded to four decimals.
 */
fun Number.to4decimalResFloat(): Float {
    return this.to4decimalResFloat(Locale.getDefault())
}

/**
 * Converts the number to a [Float] rounded to four decimal places
 * using the specified [locale].
 *
 * The number is first formatted as a string with four decimals,
 * then parsed back to a float to ensure proper rounding.
 *
 * @param locale The locale used for formatting (must not be null).
 * @return The float value rounded to four decimals.
 *
 * @throws IllegalArgumentException if [locale] is null.
 */
fun Number.to4decimalResFloat(locale: Locale): Float {

    val formatted = "%.4f".format(locale, this)
    val decimalFormatter = DecimalFormat("###,###,###,###.##")
    return decimalFormatter.parse(formatted)?.toFloat() ?: 0.0f
}


/**
 * Formats the number according to the default locale.
 *
 * @return formatted number as [String]
 */
fun Number.toNumberFormat(): String = this.toNumberFormat(Locale.getDefault())

/**
 * Formats the number according to the specified [locale].
 *
 * @param locale the [Locale] to use for formatting
 * @return formatted number as [String]
 */
fun Number.toNumberFormat(locale: Locale): String =
    runCatching {
        NumberFormat.getNumberInstance(locale).format(this)
    }.getOrElse {
        this.toString()
    }


/**
 * Formats the number as currency according to the default locale.
 *
 * @return formatted currency string, e.g., 250 to "$250.00"
 */
fun Number.toNumberFormatCurrency(): String = this.toNumberFormatCurrency(Locale.getDefault())

/**
 * Formats the number as currency according to the specified [locale].
 *
 * @param locale the [Locale] to use for currency formatting
 * @return formatted currency string, e.g., 250 to "$250.00"
 */
fun Number.toNumberFormatCurrency(locale: Locale): String =
    runCatching {
        NumberFormat.getCurrencyInstance(locale).format(this)
    }.getOrElse {
        this.toString()
    }


/**
 * Formats the number as an integer string according to the default locale.
 *
 * @return formatted integer string, e.g., 250.10 to "250"
 */
fun Number.toNumberFormatInt(): String = this.toNumberFormatInt(Locale.getDefault())

/**
 * Formats the number as an integer string according to the specified [locale].
 *
 * @param locale the [Locale] to use for integer formatting
 * @return formatted integer string, e.g., 250.10 to "250"
 */
fun Number.toNumberFormatInt(locale: Locale): String =
    runCatching {
        NumberFormat.getIntegerInstance(locale).format(this)
    }.getOrElse {
        this.toString()
    }


/**
 * Formats a normalized percentage value using the default locale.
 *
 * This function expects the receiver to be a value between `0.0` and `1.0`,
 * where `1.0` represents `100%`.
 *
 * ### Example:
 * ```
 * 0.25.toPercentNormalized() // "25.00 %"
 * ```
 *
 * @param minDigits Minimum number of fraction digits.
 * @param maxDigits Maximum number of fraction digits.
 *
 * @return A localized percentage string. Returns `"0.00 %"` if formatting fails.
 *
 * @since 3.0.0
 */
fun Number?.toPercentNormalized(minDigits: Int = 2, maxDigits: Int = 2): String {
    return this.toPercentNormalized(locale = Locale.getDefault(), minDigits = minDigits, maxDigits = maxDigits)
}


/**
 * Formats a normalized percentage value using the specified locale.
 *
 * This function expects the receiver to be a value between `0.0` and `1.0`,
 * where `1.0` represents `100%`.
 *
 * ### Example:
 * ```
 * 0.1.toPercentNormalized(Locale.US) // "10.00%"
 * ```
 *
 * @param locale Locale used for formatting.
 * @param minDigits Minimum number of fraction digits.
 * @param maxDigits Maximum number of fraction digits.
 *
 * @return A localized percentage string. Returns `"0.00 %"` if formatting fails.
 *
 * @throws IllegalArgumentException if `minDigits > maxDigits`.
 *
 * @since 3.0.0
 */
fun Number?.toPercentNormalized(locale: Locale, minDigits: Int = 2, maxDigits: Int = 2): String = runCatching {
        require(minDigits <= maxDigits) { "minDigits must be less than or equal to maxDigits" }
        NumberFormat.getPercentInstance(locale).apply {
            minimumFractionDigits = minDigits
            maximumFractionDigits = maxDigits
        }.format(this ?: 0)
    }.getOrElse { "0.00 %" }


/**
 * Formats a percentage from a direct numeric value using the default locale.
 *
 * This function expects the receiver to be a value where 100 represents 100%.
 * Values greater than 100 are displayed as-is.
 *
 * ### Example:
 * ```
 * 10.toPercentFromValue() // "10.00 %"
 * ```
 *
 * @param minDigits Minimum number of fraction digits.
 * @param maxDigits Maximum number of fraction digits.
 *
 * @return A localized percentage string. Returns `"0.00 %"` if formatting fails.
 *
 * @since 3.0.0
 */
fun Number?.toPercentFromValue(minDigits: Int = 2, maxDigits: Int = 2): String {
    return this.toPercentFromValue(locale = Locale.getDefault(), minDigits = minDigits, maxDigits = maxDigits)
}


/**
 * Formats a percentage from a direct numeric value using the specified locale.
 *
 * This function expects the receiver to be a value where 100 represents 100%.
 * Values greater than 100 are displayed as-is.
 *
 * ### Examples:
 * ```
 * 50.toPercentFromValue() // "50.00 %"
 * 200.toPercentFromValue() // "200.00 %"
 * ```
 *
 * @param locale Locale used for formatting.
 * @param minDigits Minimum number of fraction digits.
 * @param maxDigits Maximum number of fraction digits.
 *
 * @return A localized percentage string. Returns `"0.00 %"` if formatting fails.
 *
 * @throws IllegalArgumentException if `minDigits > maxDigits`.
 *
 * @since 3.0.0
 */
fun Number?.toPercentFromValue(locale: Locale, minDigits: Int = 2, maxDigits: Int = 2): String = runCatching {
    require(minDigits <= maxDigits) { "minDigits must be less than or equal to maxDigits" }
    val value = (this?.toDouble() ?: 0.0) / 100.0
    NumberFormat.getPercentInstance(locale).apply {
        minimumFractionDigits = minDigits
        maximumFractionDigits = maxDigits
    }.format(value)
}.getOrElse { "0.00 %" }


/**
 * Calculates and formats the percentage that this value represents of the given total
 * using the default locale.
 *
 * ### Example:
 * ```
 * 25.percentageOf(200) // "12.50 %"
 * ```
 *
 * @param total The total value used as the denominator.
 * @param minDigits Minimum number of fraction digits.
 * @param maxDigits Maximum number of fraction digits.
 *
 * @return A localized percentage string. Returns `"0.00 %"` if calculation fails.
 *
 * @since 3.0.0
 */
fun Number?.percentageOf(total: Number?, minDigits: Int = 2, maxDigits: Int = 2): String {
    return this.percentageOf(total = total, locale = Locale.getDefault(), minDigits = minDigits, maxDigits = maxDigits)
}


/**
 * Calculates and formats the percentage that this value represents of the given total
 * using the specified locale.
 *
 * ### Example:
 * ```
 * 50.percentageOf(400, Locale.US) // "12.50%"
 * ```
 *
 * @param total The total value used as the denominator. Must not be zero.
 * @param locale Locale used for formatting.
 * @param minDigits Minimum number of fraction digits.
 * @param maxDigits Maximum number of fraction digits.
 *
 * @return A localized percentage string. Returns `"0.00 %"` if calculation fails.
 *
 * @throws IllegalArgumentException if `total` is zero or `minDigits > maxDigits`.
 *
 * @since 3.0.0
 */
fun Number?.percentageOf(total: Number?, locale: Locale, minDigits: Int = 2, maxDigits: Int = 2): String = runCatching {
    require(minDigits <= maxDigits) { "minDigits must be less than or equal to maxDigits" }
    require(total != null && total.toDouble() != 0.0) { "Total must not be null or zero when calculating percentage" }
    val value = (this?.toDouble() ?: 0.0) / total.toDouble()
    NumberFormat.getPercentInstance(locale).apply {
        minimumFractionDigits = minDigits
        maximumFractionDigits = maxDigits
    }.format(value)
}.getOrElse { "0.00 %" }


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
 * Converts the number of bytes to a human-readable string using the default locale.
 *
 * @param si whether to use SI units (base 1000) or binary units (base 1024)
 * @return human-readable string representation of the byte count, e.g., "1.5 MiB"
 */
fun Long.convertBytesToHumanReadableForm(si: Boolean = false): String =
    convertBytesToHumanReadableForm(si, Locale.getDefault())

/**
 * Converts the number of bytes to a human-readable string using the specified [locale].
 *
 * @param si whether to use SI units (base 1000) or binary units (base 1024)
 * @param locale the [Locale] to format the output string
 * @return human-readable string representation of the byte count, e.g., "1.5 MiB"
 */
fun Long.convertBytesToHumanReadableForm(locale: Locale, si: Boolean = false): String =
    runCatching {
        val unit = if (si) 1000 else 1024
        if (this < unit) return "$this B"
        val exp = (ln(toDouble()) / ln(unit.toDouble())).toInt()
        val prefix = (if (si) "kMGTPE" else "KMGTPE")[exp - 1].toString() + if (si) "" else "i"
        String.format(locale, "%.1f %sB", this / unit.toDouble().pow(exp.toDouble()), prefix)
    }.getOrElse {
        this.toString()
    }



fun Number.toNormalized(decimal: Int): Double {
    val doubleValue = this.toDouble()
    if (doubleValue.isNaN() || doubleValue.isInfinite()) {
        return doubleValue
    }
    return BigDecimal(doubleValue).setScale(decimal, RoundingMode.HALF_UP).toDouble()
}


fun Number.toNormalizedString(decimal: Int): String {
    return String.format(Locale.getDefault(), "%.${decimal}f", this.toDouble())
}


fun Number.toNormalizedString(decimal: Int, locale: Locale): String {
    return String.format(locale, "%.${decimal}f", this.toDouble())
}


