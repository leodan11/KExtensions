package com.github.leodan11.k_extensions.core

/**
 * Get the first 2 initial letters of a first and last name if it exists
 */
val String.initials: String
    get() {
        return if (this.isNotBlank() && this.length < 3) this.uppercase()
        else {
            val sorts = this.split(' ')
            when (sorts.size) {
                1 -> sorts.first().substring(0, 2).uppercase()
                2 -> "${sorts.first().substring(0, 1).uppercase()}${sorts.last().substring(0, 1).uppercase()}"
                else -> "${sorts.first().substring(0, 1).uppercase()}${sorts[2].substring(0, 1).uppercase()}"
            }
        }
    }

/**
 * Converts a string to boolean such as 'Y', 'yes', 'TRUE'
 */

fun String.toBoolean(): Boolean {
    return this != "" &&
            (this.equals("TRUE", ignoreCase = true)
                    || this.equals("Y", ignoreCase = true)
                    || this.equals("YES", ignoreCase = true))
}