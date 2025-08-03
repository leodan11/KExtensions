package com.github.leodan11.k_extensions.view

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewAnimationUtils
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.animation.ArgbEvaluatorCompat
import com.google.android.material.color.MaterialColors
import kotlin.math.max


val View.colorPrimary: Int
    get() = materialColor(com.google.android.material.R.attr.colorPrimary)

val View.colorPrimaryVariant: Int
    get() = materialColor(com.google.android.material.R.attr.colorPrimaryVariant)

val View.colorSecondary: Int
    get() = materialColor(com.google.android.material.R.attr.colorSecondary)

val View.colorSecondaryVariant: Int
    get() = materialColor(com.google.android.material.R.attr.colorSecondaryVariant)

val View.colorBackground: Int
    get() = materialColor(android.R.attr.colorBackground)

val View.colorSurface: Int
    get() = materialColor(com.google.android.material.R.attr.colorSurface)

val View.colorError: Int
    get() = materialColor(com.google.android.material.R.attr.colorError)

val View.colorOnPrimary: Int
    get() = materialColor(com.google.android.material.R.attr.colorOnPrimary)

val View.colorOnSecondary: Int
    get() = materialColor(com.google.android.material.R.attr.colorOnSecondary)

val View.colorOnBackground: Int
    get() = materialColor(com.google.android.material.R.attr.colorOnBackground)

val View.colorOnSurface: Int
    get() = materialColor(com.google.android.material.R.attr.colorOnSurface)

val View.colorOnError: Int
    get() = materialColor(com.google.android.material.R.attr.colorOnError)

val View.colorPrimaryContainer: Int
    get() = materialColor(com.google.android.material.R.attr.colorPrimaryContainer)

val View.colorOnPrimaryContainer: Int
    get() = materialColor(com.google.android.material.R.attr.colorOnPrimaryContainer)

val View.colorSecondaryContainer: Int
    get() = materialColor(com.google.android.material.R.attr.colorSecondaryContainer)

val View.colorOnSecondaryContainer: Int
    get() = materialColor(com.google.android.material.R.attr.colorOnSecondaryContainer)

val View.colorTertiary: Int
    get() = materialColor(com.google.android.material.R.attr.colorTertiary)

val View.colorOnTertiary: Int
    get() = materialColor(com.google.android.material.R.attr.colorOnTertiary)

val View.colorTertiaryContainer: Int
    get() = materialColor(com.google.android.material.R.attr.colorTertiaryContainer)

val View.colorOnTertiaryContainer: Int
    get() = materialColor(com.google.android.material.R.attr.colorOnTertiaryContainer)

val View.colorErrorContainer: Int
    get() = materialColor(com.google.android.material.R.attr.colorErrorContainer)

val View.colorOnErrorContainer: Int
    get() = materialColor(com.google.android.material.R.attr.colorOnErrorContainer)

val View.colorOutline: Int
    get() = materialColor(com.google.android.material.R.attr.colorOutline)

/*
val View.colorShadow: Int
    get() = materialColor(com.google.android.material.R.attr.colorShadow)

val View.colorInverseSurface: Int
    get() = materialColor(com.google.android.material.R.attr.colorInverseSurface)

val View.colorInverseOnSurface: Int
    get() = materialColor(com.google.android.material.R.attr.colorInverseOnSurface)
*/

val View.colorSurfaceVariant: Int
    get() = materialColor(com.google.android.material.R.attr.colorSurfaceVariant)

val View.colorOnSurfaceVariant: Int
    get() = materialColor(com.google.android.material.R.attr.colorOnSurfaceVariant)


/**
 * Create a bitmap from a view
 *
 * @receiver [View]
 *
 * @return [Bitmap]
 *
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
 * Create a circular reveal animation
 *
 * @receiver [View]
 *
 * @param revealDuration [Long] duration of the animation, default `1500L`
 * @param centerX [Int] center x, default `0`
 * @param centerY [Int] center y, default `0`
 * @param startColor [Int] start color
 * @param endColor [Int] end color
 * @param showAtEnd [Boolean] show at end, default `true`
 *
 * @return [Animator]
 *
 */
fun View.createCircularReveal(
    revealDuration: Long = 1500L,
    centerX: Int = 0,
    centerY: Int = 0,
    @ColorRes startColor: Int,
    @ColorRes endColor: Int,
    showAtEnd: Boolean = true
): Animator {

    val radius = max(width, height).toFloat()
    val startRadius = if (showAtEnd) 0f else radius
    val finalRadius = if (showAtEnd) radius else 0f

    val animator =
        ViewAnimationUtils.createCircularReveal(this, centerX, centerY, startRadius, finalRadius)
            .apply {
                interpolator = FastOutSlowInInterpolator()
                duration = revealDuration
                doOnEnd {
                    isVisible = showAtEnd
                }
                start()
            }


    ValueAnimator().apply {
        setIntValues(
            ContextCompat.getColor(context, startColor),
            ContextCompat.getColor(context, endColor)
        )
        setEvaluator(ArgbEvaluatorCompat())
        addUpdateListener { valueAnimator -> setBackgroundColor((valueAnimator.animatedValue as Int)) }
        duration = revealDuration

        start()
    }
    return animator
}

/**
 * Find a view by id
 *
 * @receiver [View]
 *
 * @param id [Int] id of the view
 *
 * @return [View]
 */
inline fun <reified T> View.findById(@IdRes id: Int): T where T : View = findViewById(id)


/**
 * Check if the view is in RTL mode
 *
 * @receiver [View]
 *
 * @return [Boolean]
 *
 */
fun View.isRtl() = layoutDirection == View.LAYOUT_DIRECTION_RTL

fun View.materialColor(@AttrRes attr: Int, defaultColor: Int = 0): Int {
    return MaterialColors.getColor(this, attr, defaultColor)
}

fun View.xAnimator(
    values: FloatArray,
    duration: Long = 300,
    repeatCount: Int = 0,
    repeatMode: Int = 0
): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.yAnimator(
    values: FloatArray,
    duration: Long = 300,
    repeatCount: Int = 0,
    repeatMode: Int = 0
): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}


fun View.zAnimator(
    values: FloatArray,
    duration: Long = 300,
    repeatCount: Int = 0,
    repeatMode: Int = 0
): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.Z, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}
