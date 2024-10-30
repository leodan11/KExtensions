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
 * Get Background Color Default Theme Material Design
 *
 * @return [Int] - Color value
 */
fun Context.backgroundColor(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(
        com.google.android.material.R.attr.backgroundColor,
        typedValue,
        true
    )
    return typedValue.data
}


/**
 * Get OnBackground Color Default Theme Material Design
 *
 * @return [Int] - Color value
 */
fun Context.colorOnBackground(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(
        com.google.android.material.R.attr.colorOnBackground,
        typedValue,
        true
    )
    return typedValue.data
}


/**
 * Get color custom resource theme
 *
 * @param idAttrRes ID Resource, e.g: [android.R.attr.colorAccent]
 * @return [Int] - Color value
 */
fun Context.customColorResource(@AttrRes idAttrRes: Int): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(idAttrRes, typedValue, true)
    return typedValue.data
}


/**
 * Get Error Color Default Theme Material Design
 *
 * @return [Int] - Color value
 */
fun Context.colorError(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorError, typedValue, true)
    return typedValue.data
}


/**
 * Get OnError Color Default Theme Material Design
 *
 * @return [Int] - Color value
 */
fun Context.colorOnError(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorOnError, typedValue, true)
    return typedValue.data
}


/**
 * Get ErrorContainer Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.colorErrorContainer(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(
        com.google.android.material.R.attr.colorErrorContainer,
        typedValue,
        true
    )
    return typedValue.data
}


/**
 * Get OnErrorContainer Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.colorOnErrorContainer(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(
        com.google.android.material.R.attr.colorOnErrorContainer,
        typedValue,
        true
    )
    return typedValue.data
}


/**
 * Get Outline Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.colorOutline(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorOutline, typedValue, true)
    return typedValue.data
}


/**
 * Get OutlineVariant Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.colorOutlineVariant(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(
        com.google.android.material.R.attr.colorOutlineVariant,
        typedValue,
        true
    )
    return typedValue.data
}


/**
 * Get Primary Color Default Theme Material Design
 *
 * @return [Int] - Color value
 */
fun Context.colorPrimary(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true)
    return typedValue.data
}

/**
 * Get PrimaryInverse Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.colorPrimaryInverse(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(
        com.google.android.material.R.attr.colorPrimaryInverse,
        typedValue,
        true
    )
    return typedValue.data
}


/**
 * Get OnPrimary Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.colorOnPrimary(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorOnPrimary, typedValue, true)
    return typedValue.data
}


/**
 * Get PrimaryContainer Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.colorPrimaryContainer(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(
        com.google.android.material.R.attr.colorPrimaryContainer,
        typedValue,
        true
    )
    return typedValue.data
}


/**
 * Get OnPrimaryContainer Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.colorOnPrimaryContainer(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(
        com.google.android.material.R.attr.colorOnPrimaryContainer,
        typedValue,
        true
    )
    return typedValue.data
}


/**
 * Get Secondary Color Default Theme Material Design
 *
 * @return [Int] - Color value
 */
fun Context.colorSecondary(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorSecondary, typedValue, true)
    return typedValue.data
}


/**
 * Get OnSecondary Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.colorOnSecondary(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(
        com.google.android.material.R.attr.colorOnSecondary,
        typedValue,
        true
    )
    return typedValue.data
}


/**
 * Get SecondaryContainer Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.colorSecondaryContainer(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(
        com.google.android.material.R.attr.colorSecondaryContainer,
        typedValue,
        true
    )
    return typedValue.data
}


/**
 * Get OnSecondaryContainer Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.colorOnSecondaryContainer(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(
        com.google.android.material.R.attr.colorOnSecondaryContainer,
        typedValue,
        true
    )
    return typedValue.data
}


/**
 * Get Surface Color Default Theme Material Design
 *
 * @return [Int] - Color value
 */
fun Context.colorSurface(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorSurface, typedValue, true)
    return typedValue.data
}


/**
 * Get OnSurface Color Default Theme Material Design
 *
 * @return [Int] - Color value
 */
fun Context.colorOnSurface(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValue, true)
    return typedValue.data
}


/**
 * Get SurfaceVariant Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.colorSurfaceVariant(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(
        com.google.android.material.R.attr.colorSurfaceVariant,
        typedValue,
        true
    )
    return typedValue.data
}


/**
 * Get OnSurfaceVariant Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.colorOnSurfaceVariant(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(
        com.google.android.material.R.attr.colorOnSurfaceVariant,
        typedValue,
        true
    )
    return typedValue.data
}


/**
 * Get Tertiary Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.colorTertiary(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorTertiary, typedValue, true)
    return typedValue.data
}


/**
 * Get OnTertiary Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.colorOnTertiary(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(
        com.google.android.material.R.attr.colorOnTertiary,
        typedValue,
        true
    )
    return typedValue.data
}


/**
 * Get TertiaryContainer Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.colorTertiaryContainer(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(
        com.google.android.material.R.attr.colorTertiaryContainer,
        typedValue,
        true
    )
    return typedValue.data
}


/**
 * Get OnTertiaryContainer Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.colorOnTertiaryContainer(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(
        com.google.android.material.R.attr.colorOnTertiaryContainer,
        typedValue,
        true
    )
    return typedValue.data
}


/**
 * Adjust the alpha of a color
 *
 * @param factor [Float] - Factor to adjust the alpha
 * @return [Int] - Color value
 */
@ColorInt
fun Int.adjustAlpha(factor: Float): Int {
    val alpha = (Color.alpha(this) * factor).roundToInt()
    return Color.argb(alpha, Color.red(this), Color.green(this), Color.blue(this))
}


/**
 * Get ColorStateList
 *
 * @param color [Int] - Color value
 * @return [ColorStateList] - ColorStateList
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
