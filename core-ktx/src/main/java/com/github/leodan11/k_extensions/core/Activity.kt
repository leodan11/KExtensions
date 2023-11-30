package com.github.leodan11.k_extensions.core

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


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