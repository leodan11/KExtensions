package com.github.leodan11.k_extensions.view

import android.graphics.Rect
import android.widget.TextView

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
