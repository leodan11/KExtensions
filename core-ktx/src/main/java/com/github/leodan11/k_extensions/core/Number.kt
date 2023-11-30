package com.github.leodan11.k_extensions.core

import kotlin.math.log10
import kotlin.math.pow

/**
 * Convert numbers to human readable format.
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