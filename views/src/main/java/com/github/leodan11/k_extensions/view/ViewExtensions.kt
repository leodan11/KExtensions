package com.github.leodan11.k_extensions.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewAnimationUtils
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.animation.ArgbEvaluatorCompat
import kotlin.math.max

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
    showAtEnd: Boolean = true): Animator {

    val radius = max(width, height).toFloat()
    val startRadius = if (showAtEnd) 0f else radius
    val finalRadius = if (showAtEnd) radius else 0f

    val animator =
        ViewAnimationUtils.createCircularReveal(this, centerX, centerY, startRadius, finalRadius).apply {
            interpolator = FastOutSlowInInterpolator()
            duration = revealDuration
            doOnEnd {
                isVisible = showAtEnd
            }
            start()
        }


    ValueAnimator().apply {
        setIntValues(ContextCompat.getColor(context, startColor), ContextCompat.getColor(context, endColor))
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