@file:Suppress("DEPRECATION")

package com.github.leodan11.k_extensions.core

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.github.leodan11.k_extensions.core.content.DisplayDensity

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
 * @return [DisplayDensity]
 */
fun AppCompatActivity.getDisplayDensity(): DisplayDensity {
    val metrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(metrics)
    return when (metrics.densityDpi) {
        DisplayMetrics.DENSITY_LOW -> DisplayDensity.LDPI
        DisplayMetrics.DENSITY_MEDIUM -> DisplayDensity.MDPI
        DisplayMetrics.DENSITY_HIGH -> DisplayDensity.HDPI
        DisplayMetrics.DENSITY_XHIGH -> DisplayDensity.XHDPI
        DisplayMetrics.DENSITY_XXHIGH -> DisplayDensity.XXHDPI
        DisplayMetrics.DENSITY_XXXHIGH -> DisplayDensity.XXXHDPI
        else -> DisplayDensity.XXHDPI
    }
}