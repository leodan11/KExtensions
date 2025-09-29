package com.github.leodan11.k_extensions.view

import android.content.Context
import android.graphics.Rect
import android.text.TextPaint
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener

/**
 * Sets up an [AutoCompleteTextView] with a list of items, each represented as a pair of a value and a display string.
 *
 * This extension binds the [AutoCompleteTextView] to a list of `Pair<T, String>`, where:
 * - `T` is the actual value associated with each option.
 * - `String` is the label shown in the dropdown.
 *
 * The [onItemSelected] callback is triggered whenever a valid item is selected from the list,
 * or when the input text does not match any known item (in which case, `null` is passed).
 *
 * This version does **not** support a default pre-selected value. For that, use the overloaded version that includes `defaultValue`.
 *
 * ```kotlin
 * //example
 * enum class Color { RED, GREEN, BLUE }
 *
 * val colors = listOf(
 *     Color.RED to "Red",
 *     Color.GREEN to "Green",
 *     Color.BLUE to "Blue"
 * )
 *
 * autoCompleteTextView.setupWithItems(
 *     context = this,
 *     items = colors
 * ) { selectedColor ->
 *     Log.d("Color", "Selected color: $selectedColor")
 * }
 * ```
 *
 * @param T The type of the value associated with each dropdown item.
 * @receiver The [AutoCompleteTextView] to be configured.
 * @param context The context used to create the [ArrayAdapter].
 * @param items A list of pairs where the first value is the actual item and the second is the display label.
 * @param onItemSelected Callback invoked with the selected item of type [T], or `null` if the input doesn't match any item.
 *
 * @see AutoCompleteTextView
 * @see ArrayAdapter
 */
inline fun <T> AutoCompleteTextView.setupWithItems(context: Context, items: List<Pair<T, String>>, crossinline onItemSelected: (selectedItem: T?) -> Unit) {
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
 * Sets up an [AutoCompleteTextView] with a list of items, each represented as a pair of a value and a display string.
 *
 * This extension binds the [AutoCompleteTextView] to a list of `Pair<T, String>`, where:
 * - `T` is the actual value associated with each option.
 * - `String` is the label shown in the dropdown.
 *
 * A default value can be provided and will be pre-selected if it exists in the list.
 * The [onItemSelected] callback is triggered whenever a valid item is selected from the list,
 * or when the input is modified to a non-matching value (in which case, `null` is passed).
 *
 * ```kotlin
 * //example
 * enum class Fruit { APPLE, BANANA, ORANGE }
 *
 * val items = listOf(
 *     Fruit.APPLE to "Apple",
 *     Fruit.BANANA to "Banana",
 *     Fruit.ORANGE to "Orange"
 * )
 *
 * autoCompleteTextView.setupWithItems(
 *     context = this,
 *     items = items,
 *     defaultValue = Fruit.BANANA
 * ) { selectedFruit ->
 *     Log.d("Fruit", "Selected: $selectedFruit")
 * }
 * ```
 *
 * @param T The type of the value associated with each dropdown item.
 * @receiver The [AutoCompleteTextView] being configured.
 * @param context The context used to create the [ArrayAdapter].
 * @param items A list of pairs, where the first value is the actual item and the second is the display label.
 * @param defaultValue The default value to be pre-selected (if present in the list).
 * @param onItemSelected A callback invoked with the selected item of type [T], or `null` if the input doesn't match any item.
 *
 * @see AutoCompleteTextView
 * @see ArrayAdapter
 */
inline fun <T> AutoCompleteTextView.setupWithItems(context: Context, items: List<Pair<T, String>>, defaultValue: T, crossinline onItemSelected: (selectedItem: T?) -> Unit) {
    val displayNames = items.map { it.second }

    val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, displayNames)
    setAdapter(adapter)

    setOnItemClickListener { parent, _, position, _ ->
        val selectedName = parent.getItemAtPosition(position) as? String
        val selectedItem = items.find { it.second == selectedName }?.first
        onItemSelected(selectedItem)
    }

    addTextChangedListener {
        val currentText = it?.toString()
        val isValidSelection = items.any { pair -> pair.second == currentText }
        if (!isValidSelection) {
            onItemSelected(null)
        }
    }

    defaultValue?.let { value ->
        val matchingPair = items.find { it.first == value }
        matchingPair?.let {
            setText(it.second, false)
            onItemSelected(it.first)
        }
    }
}


/**
 * Calculates the bounding rectangle of the given text string when rendered with this [TextView]'s current text size and style.
 *
 * This function uses the [TextView]'s current [TextPaint] to compute the dimensions (width and height)
 * that the provided [textString] would occupy when rendered. The result is returned as a [Rect] object.
 *
 * It can be useful for layout calculations, animations, or custom drawing scenarios where
 * you need to know the actual pixel size of a given string in a [TextView]'s style.
 *
 * ```kotlin
 * //example
 * val textView = TextView(context).apply {
 *     textSize = 16f
 *     typeface = Typeface.DEFAULT_BOLD
 * }
 *
 * val bounds = textView.onTextViewTextSize("Hello World")
 * Log.d("TextBounds", "Width: ${bounds.width()}, Height: ${bounds.height()}")
 * ```
 *
 * @receiver The [TextView] whose [TextPaint] will be used to measure the text.
 * @param textString The text string to measure.
 * @return A [Rect] containing the width and height of the rendered text.
 */
fun TextView.onTextViewTextSize(textString: String): Rect {
    val bounds = Rect()
    val paint = this.paint
    paint.getTextBounds(textString, 0, textString.length, bounds)
    return bounds
}


/**
 * Updates the current selection of an [AutoCompleteTextView] based on a new value.
 *
 * This function searches for a matching item in the provided list based on the [newValue].
 * If a match is found, the display text of the [AutoCompleteTextView] is updated accordingly,
 * and the [onItemSelected] callback is triggered with the matched value.
 *
 * If the [newValue] does not exist in the list, nothing is updated and the callback is not invoked.
 *
 * ```kotlin
 * //example
 * enum class Country { USA, CANADA, MEXICO }
 *
 * val countries = listOf(
 *     Country.USA to "United States",
 *     Country.CANADA to "Canada",
 *     Country.MEXICO to "Mexico"
 * )
 *
 * // Later, update selection programmatically
 * autoCompleteTextView.updateSelection(
 *     items = countries,
 *     newValue = Country.MEXICO
 * ) { selected ->
 *     Log.d("Country", "Programmatically selected: $selected")
 * }
 * ```
 *
 * @param T The type of the value associated with each dropdown item.
 * @receiver The [AutoCompleteTextView] whose selection is being updated.
 * @param items A list of pairs where the first value is the actual item and the second is the display label.
 * @param newValue The value to select from the list, if found.
 * @param onItemSelected Callback invoked with the matched item of type [T], or not invoked if no match is found.
 */
fun <T> AutoCompleteTextView.updateSelection(items: List<Pair<T, String>>, newValue: T, onItemSelected: (T?) -> Unit) {
    val matchingPair = items.find { it.first == newValue }
    matchingPair?.let {
        setText(it.second, false)
        onItemSelected(it.first)
    }
}
