package com.github.leodan11.k_extensions.view

import android.text.TextWatcher
import android.widget.EditText
import androidx.core.widget.addTextChangedListener



/**
 * Adds a [TextWatcher] to this [EditText] and invokes the given [action]
 * after the text has changed.
 *
 * The callback receives the current text content as a [String]. If the text
 * is null, an empty string is provided instead.
 *
 * @param action Callback invoked after the text changes.
 */
fun EditText.doAfterTextChanged(action: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) = Unit

        override fun onTextChanged(
            s: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) = Unit

        override fun afterTextChanged(s: Editable?) {
            action(s?.toString().orEmpty())
        }
    })
}


/**
 * Adds a [TextWatcher] to this [EditText] and invokes the given [action]
 * immediately before the text is modified.
 *
 * The callback receives the current text content as a [String]. If the text
 * is null, an empty string is provided instead.
 *
 * @param action Callback invoked before the text changes.
 */
fun EditText.doBeforeTextChanged(action: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
            action(s?.toString().orEmpty())
        }

        override fun onTextChanged(
            s: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) = Unit

        override fun afterTextChanged(s: Editable?) = Unit
    })
}


/**
 * Adds a [TextWatcher] to this [EditText] and invokes the given [action]
 * whenever the text changes.
 *
 * The callback receives the current text content as a [String] along with the
 * change metadata provided by [TextWatcher.onTextChanged]. If the text is null,
 * an empty string is provided instead.
 *
 * @param action Callback invoked when the text changes.
 * The parameters represent:
 * - text: The current text content.
 * - start: The start position of the change.
 * - before: The length of the old text that was replaced.
 * - count: The length of the new text that replaced the old text.
 */
fun EditText.doOnTextChanged(
    action: (text: String, start: Int, before: Int, count: Int) -> Unit
) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) = Unit

        override fun onTextChanged(
            s: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
            action(
                s?.toString().orEmpty(),
                start,
                before,
                count
            )
        }

        override fun afterTextChanged(s: Editable?) = Unit
    })
}


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
