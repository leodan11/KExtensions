package com.github.leodan11.k_extensions.view

import android.content.res.ColorStateList
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.MaterialShapeDrawable

/**
 * On animated vector drawable
 *
 * @receiver [ImageView]
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
 * @receiver [ImageView]
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
 * Applies a custom-shaped background to a [ShapeableImageView] using its `shapeAppearanceModel`.
 *
 * This extension allows you to set:
 *  - A fill color.
 *
 * The background will respect any shape defined in `shapeAppearanceModel`,
 * such as circles, rounded corners, or custom cut corners.
 *
 * @param fillColor The color to fill inside the shape.
 *
 * ## sample
 * ```kotlin
 * // Blue fill with no border
 * imageView.setBackgroundShapeColor(fillColor = Color.BLUE)
 * ```
 *
 * @since 2.2.7
 */
fun ShapeableImageView.setBackgroundShapeColor(@ColorInt fillColor: Int) {
    this.setBackgroundShapeColor(fillColor = fillColor, strokeWidth = null, strokeColor = null)
}


/**
 * Applies a custom-shaped background to a [ShapeableImageView] using its `shapeAppearanceModel`.
 *
 * This extension allows you to set:
 *  - A fill color.
 *  - Optionally, a border with a specific color and width.
 *
 * The background will respect any shape defined in `shapeAppearanceModel`,
 * such as circles, rounded corners, or custom cut corners.
 *
 * @param fillColor The color to fill inside the shape.
 * @param strokeWidth Optional [Float]. The border width in pixels. If `null`, no border is applied.
 * @param strokeColor Optional [Int]. The border color. Only applied if [strokeWidth] is not `null`.
 *
 * ## sample
 *
 * ```kotlin
 * // White fill with a 4px black border
 * imageView.setBackgroundShapeColor(
 *     fillColor = Color.WHITE,
 *     strokeWidth = 4f,
 *     strokeColor = Color.BLACK
 * )
 * ```
 *
 * @since 2.2.7
 */
fun ShapeableImageView.setBackgroundShapeColor(@ColorInt fillColor: Int, strokeWidth: Float?, @ColorInt strokeColor: Int?) {
    val background = MaterialShapeDrawable(shapeAppearanceModel).apply {
        // Fill color
        this.fillColor = ColorStateList.valueOf(fillColor)
        // Optional border
        if (strokeWidth != null && strokeColor != null) {
            setStroke(strokeWidth, strokeColor)
        }
    }
    this.background = background
}
