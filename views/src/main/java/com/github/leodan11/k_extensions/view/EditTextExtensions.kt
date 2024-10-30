package com.github.leodan11.k_extensions.view

import android.widget.EditText
import androidx.core.widget.addTextChangedListener

/**
 * Move focus from a current element to next
 *
 * @receiver [EditText] current item
 * @param editTextDestiny Element that will receive the focus
 * @param lengthCounter [Int] Number of characters to pass focus, default 1
 *
 */
fun EditText.onCallbackRequestFocus(
    editTextDestiny: EditText,
    lengthCounter: Int = 1,
) {
    this.addTextChangedListener {
        if (it?.length == lengthCounter) editTextDestiny.requestFocus()
    }
}
