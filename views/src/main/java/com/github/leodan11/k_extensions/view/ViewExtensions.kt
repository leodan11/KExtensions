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


/**
 * Retrieves the primary variant color from the current theme's attributes.
 *
 * @return The resolved primary variant color as an ARGB integer.
 */
val View.colorPrimaryVariant: Int
    get() = materialColor(com.google.android.material.R.attr.colorPrimaryVariant)


/**
 * Retrieves the secondary color from the current theme's attributes.
 *
 * @return The resolved secondary color as an ARGB integer.
 */
val View.colorSecondary: Int
    get() = materialColor(com.google.android.material.R.attr.colorSecondary)


/**
 * Retrieves the secondary variant color from the current theme's attributes.
 *
 * @return The resolved secondary variant color as an ARGB integer.
 */
val View.colorSecondaryVariant: Int
    get() = materialColor(com.google.android.material.R.attr.colorSecondaryVariant)


/**
 * Retrieves the background color from the current theme's attributes.
 *
 * @return The resolved background color as an ARGB integer.
 */
val View.colorBackground: Int
    get() = materialColor(android.R.attr.colorBackground)


/**
 * Retrieves the surface color from the current theme's attributes.
 *
 * @return The resolved surface color as an ARGB integer.
 */
val View.colorSurface: Int
    get() = materialColor(com.google.android.material.R.attr.colorSurface)


/**
 * Retrieves the color used for content on primary surfaces from the current theme's attributes.
 *
 * @return The resolved on-primary color as an ARGB integer.
 */
val View.colorOnPrimary: Int
    get() = materialColor(com.google.android.material.R.attr.colorOnPrimary)


/**
 * Retrieves the color used for content on secondary surfaces from the current theme's attributes.
 *
 * @return The resolved on-secondary color as an ARGB integer.
 */
val View.colorOnSecondary: Int
    get() = materialColor(com.google.android.material.R.attr.colorOnSecondary)


/**
 * Retrieves the color used for content on background surfaces from the current theme's attributes.
 *
 * @return The resolved on-background color as an ARGB integer.
 */
val View.colorOnBackground: Int
    get() = materialColor(com.google.android.material.R.attr.colorOnBackground)


/**
 * Retrieves the color used for content on surface surfaces from the current theme's attributes.
 *
 * @return The resolved on-surface color as an ARGB integer.
 */
val View.colorOnSurface: Int
    get() = materialColor(com.google.android.material.R.attr.colorOnSurface)


/**
 * Retrieves the color used for content on error surfaces from the current theme's attributes.
 *
 * @return The resolved on-error color as an ARGB integer.
 */
val View.colorOnError: Int
    get() = materialColor(com.google.android.material.R.attr.colorOnError)


/**
 * Retrieves the primary container color from the current theme's attributes.
 *
 * @return The resolved primary container color as an ARGB integer.
 */
val View.colorPrimaryContainer: Int
    get() = materialColor(com.google.android.material.R.attr.colorPrimaryContainer)


/**
 * Retrieves the color used for content on primary containers from the current theme's attributes.
 *
 * @return The resolved on-primary container color as an ARGB integer.
 */
val View.colorOnPrimaryContainer: Int
    get() = materialColor(com.google.android.material.R.attr.colorOnPrimaryContainer)


/**
 * Retrieves the secondary container color from the current theme's attributes.
 *
 * @return The resolved secondary container color as an ARGB integer.
 */
val View.colorSecondaryContainer: Int
    get() = materialColor(com.google.android.material.R.attr.colorSecondaryContainer)


/**
 * Retrieves the color used for content on secondary containers from the current theme's attributes.
 *
 * @return The resolved on-secondary container color as an ARGB integer.
 */
val View.colorOnSecondaryContainer: Int
    get() = materialColor(com.google.android.material.R.attr.colorOnSecondaryContainer)


/**
 * Retrieves the tertiary color from the current theme's attributes.
 *
 * @return The resolved tertiary color as an ARGB integer.
 */
val View.colorTertiary: Int
    get() = materialColor(com.google.android.material.R.attr.colorTertiary)


/**
 * Retrieves the color used for content on tertiary surfaces from the current theme's attributes.
 *
 * @return The resolved on-tertiary color as an ARGB integer.
 */
val View.colorOnTertiary: Int
    get() = materialColor(com.google.android.material.R.attr.colorOnTertiary)


/**
 * Retrieves the tertiary container color from the current theme's attributes.
 *
 * @return The resolved tertiary container color as an ARGB integer.
 */
val View.colorTertiaryContainer: Int
    get() = materialColor(com.google.android.material.R.attr.colorTertiaryContainer)


/**
 * Retrieves the color used for content on tertiary containers from the current theme's attributes.
 *
 * @return The resolved on-tertiary container color as an ARGB integer.
 */
val View.colorOnTertiaryContainer: Int
    get() = materialColor(com.google.android.material.R.attr.colorOnTertiaryContainer)


/**
 * Retrieves the error container color from the current theme's attributes.
 *
 * @return The resolved error container color as an ARGB integer.
 */
val View.colorErrorContainer: Int
    get() = materialColor(com.google.android.material.R.attr.colorErrorContainer)


/**
 * Retrieves the color used for content on error containers from the current theme's attributes.
 *
 * @return The resolved on-error container color as an ARGB integer.
 */
val View.colorOnErrorContainer: Int
    get() = materialColor(com.google.android.material.R.attr.colorOnErrorContainer)


/**
 * Retrieves the outline color from the current theme's attributes.
 *
 * @return The resolved outline color as an ARGB integer.
 */
val View.colorOutline: Int
    get() = materialColor(com.google.android.material.R.attr.colorOutline)


/**
 * Retrieves the surface variant color from the current theme's attributes.
 *
 * @return The resolved surface variant color as an ARGB integer.
 */
val View.colorSurfaceVariant: Int
    get() = materialColor(com.google.android.material.R.attr.colorSurfaceVariant)


/**
 * Retrieves the color used for content on surface variants from the current theme's attributes.
 *
 * @return The resolved on-surface variant color as an ARGB integer.
 */
val View.colorOnSurfaceVariant: Int
    get() = materialColor(com.google.android.material.R.attr.colorOnSurfaceVariant)


/**
 * Creates a `Bitmap` representation of the current `View`. This method captures the visual content of
 * the `View` as a `Bitmap`, which can then be used for image manipulation, caching, or sharing purposes.
 *
 * The resulting bitmap will have the same width and height as the view, and the drawing will be done
 * using the ARGB_8888 configuration for high-quality image representation.
 *
 * ```kotlin
 * val viewBitmap = view.createBitmap()
 * imageView.setImageBitmap(viewBitmap)
 * ```
 *
 * @return A `Bitmap` object representing the visual content of the view.
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
 * Creates a circular reveal animation for the `View` with the option to animate its background color.
 * The view will be revealed or hidden with a smooth circular transition from the specified `startColor`
 * to the `endColor`.
 *
 * The center of the circular reveal can be customized by providing `centerX` and `centerY`. By default,
 * the reveal will occur from the top-left corner (0,0).
 *
 *
 * ```kotlin
 * view.createCircularReveal(
 *     revealDuration = 1000L,
 *     startColor = R.color.colorStart,
 *     endColor = R.color.colorEnd
 * )
 * ```
 *
 *
 * @param revealDuration Duration of the circular reveal animation in milliseconds. Default is 1500 ms.
 * @param centerX The X coordinate of the center of the circular reveal. Default is 0.
 * @param centerY The Y coordinate of the center of the circular reveal. Default is 0.
 * @param startColor The starting color for the reveal background.
 * @param endColor The ending color for the reveal background.
 * @param showAtEnd Whether to show the view after the reveal animation completes. Default is `true`.
 *
 * @return The `Animator` object to control the circular reveal animation.
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

    // Animating the background color change during the reveal animation
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
 * A more convenient and type-safe version of `findViewById` for `View` objects.
 * This inline function uses reified types to avoid the need for casting when retrieving a view by its ID.
 *
 * ```kotlin
 * val button: Button = view.findById(R.id.button)
 * ```
 *
 * @param id The resource ID of the `View` to be found.
 * @return The view of type `T` corresponding to the given ID.
 *
 * @throws ClassCastException If the view with the provided ID is not of the specified type.
 */
inline fun <reified T> View.findById(@IdRes id: Int): T where T : View = findViewById(id)


/**
 * Checks whether the `View` is laid out in a Right-to-Left (RTL) direction.
 * This can be useful for determining the layout direction based on the device's locale
 * or user preferences.
 *
 * ```kotlin
 * if (view.isRtl()) {
 *     // Handle RTL layout
 * }
 * ```
 *
 * @return `true` if the `View` is laid out in RTL direction, `false` otherwise.
 */
fun View.isRtl() = layoutDirection == View.LAYOUT_DIRECTION_RTL


/**
 * Retrieves a color from the view's current theme attributes using [MaterialColors].
 *
 * ```kotlin
 * val errorColor = view.materialColor(com.google.android.material.R.attr.colorError, Color.RED)
 * ```
 *
 * @param attr The attribute resource ID of the color to retrieve.
 * @param defaultColor The fallback color to use if the attribute is not found. Default is 0.
 * @return The resolved color as an ARGB integer.
 */
fun View.materialColor(@AttrRes attr: Int, defaultColor: Int = 0): Int {
    return MaterialColors.getColor(this, attr, defaultColor)
}


/**
 * Creates an `ObjectAnimator` for animating the X axis (translation) of a `View` with the specified values.
 * The animator will modify the X property of the view based on the given `values` over a specified duration.
 *
 * You can control the repeat behavior and the duration of the animation.
 *
 * ```kotlin
 * val xAnimation = view.xAnimator(floatArrayOf(0f, 300f), duration = 500)
 * xAnimation.start()
 * ```
 *
 * @param values The values that the X property will animate through.
 * @param duration The duration of the animation in milliseconds. Default is 300 ms.
 * @param repeatCount The number of times to repeat the animation. Default is 0 (no repeats).
 * @param repeatMode The repeat mode for the animation. Either [ObjectAnimator.REVERSE] or [ObjectAnimator.RESTART]. Default is 0 (no repeat).
 * @return The `Animator` object to control the animation.
 */
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


/**
 * Creates an `ObjectAnimator` for animating the Y axis (translation) of a `View` with the specified values.
 * The animator will modify the Y property of the view based on the given `values` over a specified duration.
 *
 * You can control the repeat behavior and the duration of the animation.
 *
 * ```kotlin
 * val yAnimation = view.yAnimator(floatArrayOf(0f, 500f), duration = 500)
 * yAnimation.start()
 * ```
 *
 * @param values The values that the Y property will animate through.
 * @param duration The duration of the animation in milliseconds. Default is 300 ms.
 * @param repeatCount The number of times to repeat the animation. Default is 0 (no repeats).
 * @param repeatMode The repeat mode for the animation. Either [ObjectAnimator.REVERSE] or [ObjectAnimator.RESTART]. Default is 0 (no repeat).
 * @return The `Animator` object to control the animation.
 */
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


/**
 * Creates an `ObjectAnimator` for animating the Z axis (elevation) of a `View` with the specified values.
 * The animator will modify the Z property of the view based on the given `values` over a specified duration.
 *
 * You can control the repeat behavior and the duration of the animation.
 *
 * ```kotlin
 * val zAnimation = view.zAnimator(floatArrayOf(0f, 10f), duration = 500)
 * zAnimation.start()
 * ```
 *
 * @param values The values that the Z property will animate through.
 * @param duration The duration of the animation in milliseconds. Default is 300 ms.
 * @param repeatCount The number of times to repeat the animation. Default is 0 (no repeats).
 * @param repeatMode The repeat mode for the animation. Either [ObjectAnimator.REVERSE] or [ObjectAnimator.RESTART]. Default is 0 (no repeat).
 * @return The `Animator` object to control the animation.
 */
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
