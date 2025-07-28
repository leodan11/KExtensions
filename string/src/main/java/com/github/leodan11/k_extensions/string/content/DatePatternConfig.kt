package com.github.leodan11.k_extensions.string.content

/**
 * This class provides configurations for date patterns and their corresponding formats.
 *
 * It includes predefined date patterns and formats, and allows users to add custom patterns through a builder.
 * Additionally, it offers functionality to infer the format of a given date string based on its pattern.
 *
 * @property patternToFormat A mutable list of pairs where each pair consists of a regular expression pattern and its corresponding date format.
 *
 * @constructor Creates an instance of [DatePatternConfig] with the provided list of patterns and formats.
 *
 * The following patterns are supported by default:
 * - `^\\d{4}-\\d{2}-\\d{2}$` -> `yyyy-MM-dd`
 * - `^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$` -> `yyyy-MM-dd HH:mm:ss`
 * - `^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$` -> `yyyy-MM-dd'T'HH:mm:ss`
 * - `^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}$` -> `yyyy-MM-dd'T'HH:mm:ss.SSS`
 * - `^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[+-]\\d{2}:\\d{2}$` -> `yyyy-MM-dd'T'HH:mm:ssXXX`
 * - `^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}[+-]\\d{2}:\\d{2}$` -> `yyyy-MM-dd'T'HH:mm:ss.SSSXXX`
 * - `^\\d{2}:\\d{2}:\\d{2}$` -> `HH:mm:ss`
 * - `^\\d{4}-\\d{2}$` -> `yyyy-MM`
 * - `^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$` -> `yyyy-MM-dd HH:mm`
 * - `^\\d{2}/\\d{2}/\\d{4}$` -> `dd/MM/yyyy`
 */
class DatePatternConfig private constructor(
    private val patternToFormat: MutableList<Pair<String, String>> = mutableListOf(
        "^\\d{4}-\\d{2}-\\d{2}$" to "yyyy-MM-dd",
        "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$" to "yyyy-MM-dd HH:mm:ss",
        "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$" to "yyyy-MM-dd'T'HH:mm:ss",
        "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}$" to "yyyy-MM-dd'T'HH:mm:ss.SSS",
        "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[+-]\\d{2}:\\d{2}$" to "yyyy-MM-dd'T'HH:mm:ssXXX",
        "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}[+-]\\d{2}:\\d{2}$" to "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
        "^\\d{2}:\\d{2}:\\d{2}$" to "HH:mm:ss",
        "^\\d{4}-\\d{2}$" to "yyyy-MM",
        "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$" to "yyyy-MM-dd HH:mm",
        "^\\d{2}/\\d{2}/\\d{4}$" to "dd/MM/yyyy"
    )
) {

    /**
     * Builder class to configure a custom instance of [DatePatternConfig].
     *
     * This builder allows you to add custom patterns and formats, and then create a [DatePatternConfig] instance.
     */
    class Builder {

        private val patternToFormat: MutableList<Pair<String, String>> = mutableListOf()

        /**
         * Adds a new date pattern and its corresponding format to the configuration.
         *
         * If the pattern already exists, an [IllegalArgumentException] will be thrown.
         *
         * @param pattern A regular expression representing the date pattern.
         * @param format The corresponding date format.
         * @return The builder instance for method chaining.
         * @throws IllegalArgumentException If the pattern already exists.
         */
        fun addPattern(pattern: String, format: String): Builder {
            if (patternToFormat.any { it.first == pattern }) {
                throw IllegalArgumentException("Pattern already exists!")
            }
            patternToFormat.add(pattern to format)
            return this
        }

        /**
         * Builds and returns a [DatePatternConfig] instance with the added patterns and formats.
         *
         * @return A new instance of [DatePatternConfig] with the configured patterns.
         */
        fun build(): DatePatternConfig {
            return DatePatternConfig(patternToFormat)
        }
    }

    /**
     * Gets all the date patterns and their corresponding formats in the current configuration.
     *
     * @return A list of pairs where each pair contains a date pattern and its corresponding format.
     */
    fun getPatterns(): List<Pair<String, String>> = patternToFormat

    /**
     * Infers the date format of a given date string based on the pattern that matches it.
     *
     * This function checks the configured patterns and returns the corresponding format for the pattern
     * that matches the provided date string.
     *
     * @param date The date string to infer the format for.
     * @return The date format corresponding to the matching pattern.
     * @throws IllegalArgumentException If no matching pattern is found for the date string.
     */
    fun inferDatePattern(date: String): String {
        for ((pattern, format) in patternToFormat) {
            if (date.matches(Regex(pattern))) {
                return format
            }
        }
        throw IllegalArgumentException("Unsupported date format")
    }

    companion object {
        /**
         * Creates a default [DatePatternConfig] instance using predefined patterns.
         *
         * @return A [DatePatternConfig] with the default set of date patterns.
         */
        fun default(): DatePatternConfig {
            return DatePatternConfig()
        }
    }

}
