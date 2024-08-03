package com.github.leodan11.k_extensions.core

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat

/**
 * On animated vector drawable
 *
 */
fun ImageView.startAnimatedVectorDrawable() {
    val d: Drawable = this.drawable
    if (d is AnimatedVectorDrawableCompat) {
        val avd: AnimatedVectorDrawableCompat = d
        avd.start()
    } else if (d is AnimatedVectorDrawable) {
        val avd: AnimatedVectorDrawable = d
        avd.start()
    }
}

/**
 * On animated vector-drawable loop
 *
 * API 23 or higher is required for [AnimatedVectorDrawable]
 *
 */
fun ImageView.startAnimatedVectorDrawableLoop() {
    val d: Drawable = this.drawable
    if (d is AnimatedVectorDrawableCompat) {
        val avd: AnimatedVectorDrawableCompat = d
        avd.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                super.onAnimationEnd(drawable)
                avd.start()
            }
        })
        avd.start()
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (d is AnimatedVectorDrawable) {
                val avd: AnimatedVectorDrawable = d
                avd.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        super.onAnimationEnd(drawable)
                        avd.start()
                    }
                })
                avd.start()
            }
        }
    }
}

/**
 * Create a bitmap from a view
 *
 * @return [Bitmap]
 */
fun View.createBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(
        this.layoutParams.width,
        this.layoutParams.height,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    this.draw(canvas)
    return bitmap
}


/**
 * Move focus from a current element to next
 *
 * - [this] Current item
 *
 * @param editTextDestiny Element that will receive the focus
 * @param lengthCounter [Int] Number of characters to pass focus, default 1
 */
fun EditText.onCallbackRequestFocus(
    editTextDestiny: EditText,
    lengthCounter: Int = 1,
) {
    this.addTextChangedListener {
        if (it?.length == lengthCounter) editTextDestiny.requestFocus()
    }
}


/**
 * Get the size of the rectangle based on the text string
 *
 * - [this] Text view where you will set the rectangle
 *
 * @param textString Text string
 * @return [Rect] Holds four integer coordinates for a rectangle.
 */
fun TextView.onTextViewTextSize(textString: String): Rect {
    val bounds = Rect()
    val paint = this.paint
    paint.getTextBounds(textString, 0, textString.length, bounds)
    return bounds
}