package com.github.leodan11.k_extensions.view

import android.text.TextWatcher
import android.widget.EditText
import androidx.core.widget.addTextChangedListener

/**
 * Automatically requests focus on another [EditText] when this [EditText] reaches a specific character length.
 *
 * This extension adds a [TextWatcher] that listens to changes in the current [EditText]'s text.
 * When the number of characters equals [lengthCounter], it automatically shifts focus to [editTextDestiny].
 *
 * This is commonly used in forms such as verification codes, credit card inputs, or multi-field data entry,
 * where focus should move seamlessly between input fields.
 *
 * ```kotlin
 * //example
 * val codeField1 = findViewById<EditText>(R.id.code_1)
 * val codeField2 = findViewById<EditText>(R.id.code_2)
 *
 * // Move focus to codeField2 when codeField1 reaches 1 character
 * codeField1.onCallbackRequestFocus(editTextDestiny = codeField2)
 * ```
 *
 * @receiver The [EditText] being observed for text length.
 * @param editTextDestiny The target [EditText] that will receive focus when the condition is met.
 * @param lengthCounter The number of characters that triggers the focus change. Default is `1`.
 */
fun EditText.onCallbackRequestFocus(
    editTextDestiny: EditText,
    lengthCounter: Int = 1,
) {
    this.addTextChangedListener {
        if (it?.length == lengthCounter) editTextDestiny.requestFocus()
    }
}
