package com.github.leodan11.k_extensions.view

import android.content.Context
import android.graphics.Rect
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener

/**
 * Configures an [AutoCompleteTextView] with a list of paired data (model and display name),
 * providing a type-safe and reliable way to handle user selection.
 *
 * @param T The generic type of the underlying model for each item.
 * @param context The context used to create the [ArrayAdapter].
 * @param items A list of pairs where each pair contains a model of type [T] and its corresponding display name as a [String].
 * @param onItemSelected A callback invoked when the user selects an item, receiving the selected model or null if the selection is invalid or cleared.
 */
inline fun <T> AutoCompleteTextView.setupWithItems(
    context: Context,
    items: List<Pair<T, String>>,
    crossinline onItemSelected: (selectedItem: T?) -> Unit
) {
    val displayNames = items.map { it.second }

    val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, displayNames)
    setAdapter(adapter)

    setOnItemClickListener { parent, _, position, _ ->
        val selectedName = parent.getItemAtPosition(position) as? String
        val selectedItem = items.find { it.second == selectedName }?.first
        onItemSelected(selectedItem)
    }

    // Optionally handle manual input: clear selection if input doesn't match any item
    addTextChangedListener {
        val currentText = it?.toString()
        val isValidSelection = items.any { pair -> pair.second == currentText }
        if (!isValidSelection) {
            onItemSelected(null)
        }
    }
}


/**
 * Get the size of the rectangle based on the text string
 *
 * @receiver [TextView] where you will set the rectangle
 * @param textString Text string
 * @return [Rect] Holds four integer coordinates for a rectangle.
 *
 */
fun TextView.onTextViewTextSize(textString: String): Rect {
    val bounds = Rect()
    val paint = this.paint
    paint.getTextBounds(textString, 0, textString.length, bounds)
    return bounds
}
