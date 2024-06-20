@file:Suppress("DEPRECATION")

package com.github.leodan11.k_extensions.core

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity


/**
 * Hide input keyboard view
 *
 */
fun Activity.onHideSoftKeyboard() {
    val view: View? = this.currentFocus
    view?.clearFocus()
    if (view != null) {
        val input = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        input.hideSoftInputFromWindow(view.windowToken, 0)
    }
}


/**
 * Returns display density as ...DPI
 *
 * @return [String]
 */
fun AppCompatActivity.getDisplayDensity(): String {
    val metrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(metrics)
    return when (metrics.densityDpi) {
        DisplayMetrics.DENSITY_LOW -> "LDPI"
        DisplayMetrics.DENSITY_MEDIUM -> "MDPI"
        DisplayMetrics.DENSITY_HIGH -> "HDPI"
        DisplayMetrics.DENSITY_XHIGH -> "XHDPI"
        DisplayMetrics.DENSITY_XXHIGH -> "XXHDPI"
        DisplayMetrics.DENSITY_XXXHIGH -> "XXXHDPI"
        else -> "XXHDPI"
    }
}