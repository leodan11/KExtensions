package com.github.leodan11.k_extensions.core

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.os.Build
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

/**
 * Get resource id
 *
 * @param idAttrRes ID attr Resource, e.g: [android.R.attr.colorAccent]
 * @return [Int] - Resource Id
 */
fun Context.customResolverResourceId(@AttrRes idAttrRes: Int): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(idAttrRes, typedValue, true)
    return typedValue.resourceId
}


/**
 * Convert dp to px
 *
 * @param dp
 * @return [Float]
 */
fun Context.convertDpToPx(dp: Float): Float = (this.resources.displayMetrics.density * dp)


/**
 * Convert px to dp
 *
 * @param px
 * @return [Float]
 */
fun Context.convertPxToDp(px: Float): Float = (px / this.resources.displayMetrics.density)


/**
 * Determine if dark mode is currently active
 *
 * @return [Boolean] - It is active
 */
fun Context.isNightModeActive(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        this.resources.configuration.isNightModeActive
    } else {
        val darkModeFlag = this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        darkModeFlag == Configuration.UI_MODE_NIGHT_YES
    }
}


/**
 * Merge bitmaps
 *
 * @param bitmap
 * @return [Bitmap]
 */
fun Bitmap.mergeBitmaps(bitmap: Bitmap): Bitmap {
    val height = bitmap.height
    val width = bitmap.width

    val combined = Bitmap.createBitmap(width, height, bitmap.config)
    val canvas = Canvas(combined)
    val canvasW = canvas.width
    val canvasH = canvas.height
    canvas.drawBitmap(bitmap, Matrix(), null)

    val centreX = ((canvasW - this.width) / 2).toFloat()
    val centreY = ((canvasH - this.height) / 2).toFloat()
    canvas.drawBitmap(this, centreX, centreY, null)

    return combined
}


/**
 * Get bitmap from drawable
 *
 * @param drawableIdRes ID drawable resource
 * @return [Bitmap]
 */
fun Context.toDrawableAsBitmap(@DrawableRes drawableIdRes: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(this, drawableIdRes) ?: throw Exception("Invalid drawable")
    val canvas = Canvas()
    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    canvas.setBitmap(bitmap)
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    drawable.draw(canvas)
    return bitmap
}