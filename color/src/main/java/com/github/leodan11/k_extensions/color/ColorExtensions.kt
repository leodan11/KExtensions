package com.github.leodan11.k_extensions.color

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.github.leodan11.k_extensions.core.tag
import kotlin.math.roundToInt

/**
 * Retrieves the `colorAccent` from AppCompat theme attributes.
 *
 * This is useful for supporting legacy themes or apps that still define `colorAccent`,
 * even though it has been deprecated in Material 3 and is no longer part of modern theme attributes.
 *
 * Example:
 * ```kotlin
 * val accentColor = context.colorAccent()
 * ```
 *
 * @return The resolved accent color as an ARGB integer, or `Color.TRANSPARENT` if not defined.
 * @since 2.2.0
 */
@ColorInt
fun Context.colorAccent(): Int {
    return customColorResource(androidx.appcompat.R.attr.colorAccent)
}

/**
 * Retrieves the background color from the current theme's attributes.
 *
 * ```kotlin
 * val bgColor = context.backgroundColor()
 * ```
 *
 * @return The resolved background color as an ARGB integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.backgroundColor(): Int {
    return customColorResource(com.google.android.material.R.attr.backgroundColor)
}


/**
 * Retrieves the color used for content displayed on top of the background color from the current theme's attributes.
 *
 * ```kotlin
 * val onBackgroundColor = context.colorOnBackground()
 * ```
 *
 * @return The resolved ARGB color integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.colorOnBackground(): Int {
    return customColorResource(com.google.android.material.R.attr.colorOnBackground)
}


/**
 * Retrieves the error color defined in AppCompat-based themes.
 *
 * This replaces the previous implementation based on
 * `com.google.android.material.R.attr.colorError`, which was removed in
 * Material Components `1.13.0`.
 *
 * Useful when using AppCompat or hybrid Material 2 themes that still define `colorError`.
 *
 * Example:
 * ```kotlin
 * val errorColor = context.colorError()
 * ```
 *
 * @return The resolved error color from the current theme, or `Color.TRANSPARENT` if not defined.
 * @since 2.2.0
 */
@ColorInt
fun Context.colorError(): Int {
    return customColorResource(androidx.appcompat.R.attr.colorError)
}


/**
 * Retrieves the color used for content displayed on top of error surfaces from the current theme's attributes.
 *
 * ```kotlin
 * val onErrorColor = context.colorOnError()
 * ```
 *
 * @return The resolved ARGB color integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.colorOnError(): Int {
    return customColorResource(com.google.android.material.R.attr.colorOnError)
}


/**
 * Retrieves the color used for error container surfaces from the current theme's attributes.
 *
 * ```kotlin
 * val errorContainerColor = context.colorErrorContainer()
 * ```
 *
 * @return The resolved ARGB color integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.colorErrorContainer(): Int {
    return customColorResource(com.google.android.material.R.attr.colorErrorContainer)
}


/**
 * Retrieves the color used for content displayed on top of error container surfaces from the current theme's attributes.
 *
 * ```kotlin
 * val onErrorContainerColor = context.colorOnErrorContainer()
 * ```
 *
 * @return The resolved ARGB color integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.colorOnErrorContainer(): Int {
    return customColorResource(com.google.android.material.R.attr.colorOnErrorContainer)
}


/**
 * Retrieves the outline color used in the current theme's attributes.
 *
 * ```kotlin
 * val outlineColor = context.colorOutline()
 * ```
 *
 * @return The resolved ARGB color integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.colorOutline(): Int {
    return customColorResource(com.google.android.material.R.attr.colorOutline)
}


/**
 * Retrieves the variant outline color from the current theme's attributes.
 *
 * ```kotlin
 * val outlineVariantColor = context.colorOutlineVariant()
 * ```
 *
 * @return The resolved ARGB color integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.colorOutlineVariant(): Int {
    return customColorResource(com.google.android.material.R.attr.colorOutlineVariant)
}


/**
 * Retrieves the primary color defined in AppCompat-based themes.
 *
 * This replaces the previous implementation based on
 * `com.google.android.material.R.attr.colorPrimary`, which was removed in
 * Material Components `1.13.0`.
 *
 * Useful when using AppCompat or hybrid Material 2 themes that still define `colorPrimary`.
 *
 * Example:
 * ```kotlin
 * val primaryColor = context.colorPrimary()
 * ```
 *
 * @return The resolved primary color from the current theme, or `Color.TRANSPARENT` if not defined.
 * @since 2.2.0
 */
@ColorInt
fun Context.colorPrimary(): Int {
    return customColorResource(androidx.appcompat.R.attr.colorPrimary)
}


/**
 * Retrieves the inverse primary color from the current theme's attributes.
 *
 * ```kotlin
 * val primaryInverseColor = context.colorPrimaryInverse()
 * ```
 *
 * @return The resolved ARGB color integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.colorPrimaryInverse(): Int {
    return customColorResource(com.google.android.material.R.attr.colorPrimaryInverse)
}


/**
 * Retrieves the color used for content displayed on top of the primary color from the current theme's attributes.
 *
 * ```kotlin
 * val onPrimaryColor = context.colorOnPrimary()
 * ```
 *
 * @return The resolved ARGB color integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.colorOnPrimary(): Int {
    return customColorResource(com.google.android.material.R.attr.colorOnPrimary)
}


/**
 * Retrieves the primary container color from the current theme's attributes.
 *
 * ```kotlin
 * val primaryContainerColor = context.colorPrimaryContainer()
 * ```
 *
 * @return The resolved ARGB color integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.colorPrimaryContainer(): Int {
    return customColorResource(com.google.android.material.R.attr.colorPrimaryContainer)
}


/**
 * Retrieves the color used for content displayed on top of the primary container color from the current theme's attributes.
 *
 * ```kotlin
 * val onPrimaryContainerColor = context.colorOnPrimaryContainer()
 * ```
 *
 * @return The resolved ARGB color integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.colorOnPrimaryContainer(): Int {
    return customColorResource(com.google.android.material.R.attr.colorOnPrimaryContainer)
}


/**
 * Retrieves the secondary color from the current theme's attributes.
 *
 * ```kotlin
 * val secondaryColor = context.colorSecondary()
 * ```
 *
 * @return The resolved ARGB color integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.colorSecondary(): Int {
    return customColorResource(com.google.android.material.R.attr.colorSecondary)
}


/**
 * Retrieves the color used for content displayed on top of the secondary color from the current theme's attributes.
 *
 * ```kotlin
 * val onSecondaryColor = context.colorOnSecondary()
 * ```
 *
 * @return The resolved ARGB color integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.colorOnSecondary(): Int {
    return customColorResource(com.google.android.material.R.attr.colorOnSecondary)
}


/**
 * Retrieves the secondary container color from the current theme's attributes.
 *
 * ```kotlin
 * val secondaryContainerColor = context.colorSecondaryContainer()
 * ```
 *
 * @return The resolved ARGB color integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.colorSecondaryContainer(): Int {
    return customColorResource(com.google.android.material.R.attr.colorSecondaryContainer)
}


/**
 * Retrieves the color used for content displayed on top of the secondary container color from the current theme's attributes.
 *
 * ```kotlin
 * val onSecondaryContainerColor = context.colorOnSecondaryContainer()
 * ```
 *
 * @return The resolved ARGB color integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.colorOnSecondaryContainer(): Int {
    return customColorResource(com.google.android.material.R.attr.colorOnSecondaryContainer)
}


/**
 * Retrieves the surface color from the current theme's attributes.
 *
 * ```kotlin
 * val surfaceColor = context.colorSurface()
 * ```
 *
 * @return The resolved ARGB color integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.colorSurface(): Int {
    return customColorResource(com.google.android.material.R.attr.colorSurface)
}


/**
 * Retrieves the color used for content displayed on top of surface color from the current theme's attributes.
 *
 * ```kotlin
 * val onSurfaceColor = context.colorOnSurface()
 * ```
 *
 * @return The resolved ARGB color integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.colorOnSurface(): Int {
    return customColorResource(com.google.android.material.R.attr.colorOnSurface)
}


/**
 * Retrieves the surface variant color from the current theme's attributes.
 *
 * ```kotlin
 * val surfaceVariantColor = context.colorSurfaceVariant()
 * ```
 *
 * @return The resolved ARGB color integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.colorSurfaceVariant(): Int {
    return customColorResource(com.google.android.material.R.attr.colorSurfaceVariant)
}


/**
 * Retrieves the color used for content displayed on top of the surface variant color from the current theme's attributes.
 *
 * ```kotlin
 * val onSurfaceVariantColor = context.colorOnSurfaceVariant()
 * ```
 *
 * @return The resolved ARGB color integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.colorOnSurfaceVariant(): Int {
    return customColorResource(com.google.android.material.R.attr.colorOnSurfaceVariant)
}


/**
 * Retrieves the tertiary color from the current theme's attributes.
 *
 * ```kotlin
 * val tertiaryColor = context.colorTertiary()
 * ```
 *
 * @return The resolved ARGB color integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.colorTertiary(): Int {
    return customColorResource(com.google.android.material.R.attr.colorTertiary)
}


/**
 * Retrieves the color used for content displayed on top of the tertiary color from the current theme's attributes.
 *
 * ```kotlin
 * val onTertiaryColor = context.colorOnTertiary()
 * ```
 *
 * @return The resolved ARGB color integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.colorOnTertiary(): Int {
    return customColorResource(com.google.android.material.R.attr.colorOnTertiary)
}


/**
 * Retrieves the tertiary container color from the current theme's attributes.
 *
 * ```kotlin
 * val tertiaryContainerColor = context.colorTertiaryContainer()
 * ```
 *
 * @return The resolved ARGB color integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.colorTertiaryContainer(): Int {
    return customColorResource(com.google.android.material.R.attr.colorTertiaryContainer)
}


/**
 * Retrieves the color used for content displayed on top of the tertiary container color from the current theme's attributes.
 *
 * ```kotlin
 * val onTertiaryContainerColor = context.colorOnTertiaryContainer()
 * ```
 *
 * @return The resolved ARGB color integer.
 * If the attribute is not found, returns transparent (0).
 */
@ColorInt
fun Context.colorOnTertiaryContainer(): Int {
    return customColorResource(com.google.android.material.R.attr.colorOnTertiaryContainer)
}


/**
 * Adjusts the alpha (transparency) of a color by a given factor.
 * The factor should be between 0 and 1, where 0 is fully transparent and 1 is fully opaque.
 *
 * ```kotlin
 * val adjustedColor = originalColor.adjustAlpha(0.5f)
 * ```
 *
 * @param factor The factor by which to adjust the alpha. A value between 0 (fully transparent) and 1 (fully opaque).
 * @return The resulting ARGB color integer with the adjusted alpha value.
 */
@ColorInt
fun Int.adjustAlpha(factor: Float): Int {
    val alpha = (Color.alpha(this) * factor).roundToInt()
    return Color.argb(alpha, Color.red(this), Color.green(this), Color.blue(this))
}


/**
 * Creates a `ColorStateList` based on the provided color. It defines the colors for different states
 * such as enabled, disabled, and checked states.
 *
 * The color's alpha is adjusted for disabled states, and the checked state will keep the base color.
 *
 * ```kotlin
 * val colorStateList = context.colorStateList(Color.RED)
 * ```
 *
 * @param color The base color to create the `ColorStateList` from.
 * @return A `ColorStateList` containing colors for various states.
 */
fun Context.colorStateList(@ColorInt color: Int): ColorStateList {
    val disabledColor = color.adjustAlpha(0.3f)
    Log.i(this.tag(), "colorStateList $disabledColor")
    return ColorStateList(
        arrayOf(
            intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_enabled, android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_enabled, -android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_enabled, android.R.attr.state_checked)
        ),
        intArrayOf(color.adjustAlpha(0.8f), color, disabledColor, disabledColor)
    )
}


private fun Context.customColorResource(@AttrRes idAttrRes: Int, fallbackColor: Int = 0): Int {
    val typedValue = TypedValue()
    val resolved = this.theme.resolveAttribute(idAttrRes, typedValue, true)
    return if (resolved) typedValue.data else fallbackColor
}
