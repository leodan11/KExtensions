@file:JvmName("BitmapExtensions")

package com.github.leodan11.k_extensions.core

import android.graphics.Bitmap
import android.graphics.BlendMode
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.DITHER_FLAG
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuff.Mode
import android.graphics.PorterDuffColorFilter
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.core.graphics.createBitmap
import androidx.core.graphics.get
import androidx.core.graphics.set
import com.github.leodan11.k_extensions.core.content.Corner
import com.github.leodan11.k_extensions.core.content.WatermarkOptions
import kotlin.math.abs


/**
 * Adds a textual watermark to the receiver [Bitmap].
 *
 * This function creates a mutable copy of the original bitmap, draws the provided
 * watermark text according to the given [WatermarkOptions], and returns the resulting bitmap.
 *
 * The original bitmap is never modified.
 *
 * ### Usage (Kotlin)
 * ```
 * val watermarked = bitmap.addWatermark("Sample", options)
 * ```
 *
 * @receiver Bitmap to which the watermark will be applied.
 * @param watermarkText The text to be drawn as a watermark.
 * @param options Configuration options that define appearance, position and style
 * of the watermark. Defaults to [WatermarkOptions].
 *
 * @return A new [Bitmap] containing the watermark, or `null` if the bitmap
 * could not be copied.
 *
 * @since 3.0.0
 */
fun Bitmap.addWatermark(watermarkText: String, options: WatermarkOptions = WatermarkOptions()): Bitmap? {
    val config = config ?: Bitmap.Config.ARGB_8888
    val result = copy(config, true) ?: return null
    val canvas = Canvas(result)
    val paint = Paint(ANTI_ALIAS_FLAG or DITHER_FLAG)
    paint.textAlign = when (options.corner) {
        Corner.TOP_LEFT,
        Corner.BOTTOM_LEFT -> Paint.Align.LEFT

        Corner.TOP_RIGHT,
        Corner.BOTTOM_RIGHT -> Paint.Align.RIGHT
    }
    val textSize = result.width * options.textSizeToWidthRatio
    paint.textSize = textSize
    paint.color = options.textColor
    if (options.shadowColor != null) {
        paint.setShadowLayer(textSize / 2, 0f, 0f, options.shadowColor)
    }
    if (options.typeface != null) {
        paint.typeface = options.typeface
    }
    val padding = result.width * options.paddingToWidthRatio
    val coordinates =
        calculateCoordinates(watermarkText, paint, options, canvas.width, canvas.height, padding)
    canvas.drawText(watermarkText, coordinates.x, coordinates.y, paint)
    return result
}


/**
 * Adds a textual watermark to the given [Bitmap].
 *
 * This is a convenience function intended mainly for Java interoperability.
 * Internally, it delegates all logic to [Bitmap.addWatermark].
 *
 * ### Usage (Java)
 * ```
 * Bitmap result = BitmapWatermark.addWatermarkToBitmap(bitmap, "Sample");
 * ```
 *
 * @param bitmap The bitmap to which the watermark will be applied.
 * @param watermarkText The text to be drawn as a watermark.
 * @param options Configuration options for watermark appearance and position.
 *
 * @return A new [Bitmap] containing the watermark, or `null` if the bitmap
 * could not be copied.
 *
 * @since 3.0.0
 */
@JvmOverloads
fun addWatermarkToBitmap(bitmap: Bitmap, watermarkText: String, options: WatermarkOptions = WatermarkOptions()): Bitmap? = bitmap.addWatermark(watermarkText, options)


/**
 * Creates a new [Bitmap] by overlaying the receiver bitmap on top of the [baseBitmap].
 *
 * The receiver bitmap is drawn at the specified offset position with optional transparency.
 *
 * ```kotlin
 * val baseBitmap: Bitmap = ...
 * val overlayBitmap: Bitmap = ...
 * val mergedBitmap = overlayBitmap.mergeBitmaps(baseBitmap,offsetX = 50,offsetY = 100,alpha = 128)
 * ```
 *
 * @param baseBitmap The bitmap that will serve as the background.
 * @param offsetX Horizontal offset (in pixels) to draw the receiver bitmap. Defaults to centered.
 * @param offsetY Vertical offset (in pixels) to draw the receiver bitmap. Defaults to centered.
 * @param alpha Alpha value (transparency) for the receiver bitmap. Range 0 (transparent) to 255 (opaque). Defaults to 255.
 * @return A new [Bitmap] combining both bitmaps.
 *
 * @since 3.0.0
 */
fun Bitmap.mergeBitmaps(baseBitmap: Bitmap, offsetX: Float = (baseBitmap.width - this.width) / 2f, offsetY: Float = (baseBitmap.height - this.height) / 2f, alpha: Int = 255): Bitmap {
    val combined = createBitmap(
        baseBitmap.width,
        baseBitmap.height,
        baseBitmap.config ?: Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(combined)
    val paint = Paint().apply { this.alpha = alpha.coerceIn(0, 255) }
    canvas.drawBitmap(baseBitmap, Matrix(), null)
    canvas.drawBitmap(this, offsetX, offsetY, paint)
    return combined
}


/**
 * Creates a new [Bitmap] by overlaying the receiver bitmap on top of the [baseBitmap]
 * using the specified [blendMode] to combine the pixels.
 *
 * ```kotlin
 * val baseBitmap: Bitmap = ...
 * val overlayBitmap: Bitmap = ...
 * val mergedBitmap = overlayBitmap.mergeWithBlendMode(
 *     baseBitmap,
 *     blendMode = PorterDuff.Mode.MULTIPLY,
 *     alpha = 180
 * )
 * ```
 *
 * @param baseBitmap The bitmap that will serve as the background.
 * @param blendMode The [PorterDuff.Mode] to use when blending the receiver bitmap on top of the base bitmap.
 *                  Defaults to [PorterDuff.Mode.SRC_OVER] (normal drawing).
 * @param offsetX Horizontal offset (in pixels) to draw the receiver bitmap. Defaults to centered.
 * @param offsetY Vertical offset (in pixels) to draw the receiver bitmap. Defaults to centered.
 * @param alpha Alpha value (transparency) for the receiver bitmap. Range 0 (transparent) to 255 (opaque). Defaults to 255.
 * @return A new [Bitmap] combining both bitmaps with the blend mode applied.
 *
 * @since 3.0.0
 */
fun Bitmap.mergeWithBlendMode(baseBitmap: Bitmap, blendMode: Mode = Mode.SRC_OVER, offsetX: Float = (baseBitmap.width - this.width) / 2f, offsetY: Float = (baseBitmap.height - this.height) / 2f, alpha: Int = 255): Bitmap {
    val combined = createBitmap(
        baseBitmap.width,
        baseBitmap.height,
        baseBitmap.config ?: Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(combined)
    val paint = Paint().apply {
        this.alpha = alpha.coerceIn(0, 255)
        this.xfermode = PorterDuffXfermode(blendMode)
        isAntiAlias = true
        isFilterBitmap = true
    }
    // Draw base bitmap first without blend mode
    canvas.drawBitmap(baseBitmap, Matrix(), null)
    // Draw receiver bitmap with blend mode
    canvas.drawBitmap(this, offsetX, offsetY, paint)
    // Clear X-fer_mode to avoid affecting subsequent draw calls
    paint.xfermode = null
    return combined
}


/**
 * Creates a new [Bitmap] by overlaying the receiver bitmap on top of the [baseBitmap]
 * using the specified [blendMode] (API 29+).
 *
 * ```kotlin
 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
 *     val baseBitmap: Bitmap = ...
 *     val overlayBitmap: Bitmap = ...
 *     val mergedBitmap = overlayBitmap.mergeWithModernBlendMode(
 *         baseBitmap,
 *         blendMode = BlendMode.MULTIPLY,
 *         alpha = 180
 *     )
 * }
 * ```
 *
 * @param baseBitmap The bitmap that will serve as the background.
 * @param blendMode The [BlendMode] to use when blending the receiver bitmap on top of the base bitmap.
 *                  Defaults to [BlendMode.SRC_OVER] (normal drawing).
 * @param offsetX Horizontal offset (in pixels) to draw the receiver bitmap. Defaults to centered.
 * @param offsetY Vertical offset (in pixels) to draw the receiver bitmap. Defaults to centered.
 * @param alpha Alpha value (transparency) for the receiver bitmap. Range 0 (transparent) to 255 (opaque). Defaults to 255.
 * @return A new [Bitmap] combining both bitmaps with the blend mode applied.
 *
 * @since 3.0.0
 *
 */
@RequiresApi(Build.VERSION_CODES.Q)
fun Bitmap.mergeWithModernBlendMode(baseBitmap: Bitmap, blendMode: BlendMode = BlendMode.SRC_OVER, offsetX: Float = (baseBitmap.width - this.width) / 2f, offsetY: Float = (baseBitmap.height - this.height) / 2f, alpha: Int = 255): Bitmap {
    val combined = createBitmap(
        baseBitmap.width,
        baseBitmap.height,
        baseBitmap.config ?: Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(combined)
    val paint = Paint().apply {
        this.alpha = alpha.coerceIn(0, 255)
        this.blendMode = blendMode
        isAntiAlias = true
        isFilterBitmap = true
    }
    // Draw base bitmap first without blend mode
    canvas.drawBitmap(baseBitmap, Matrix(), null)
    // Draw receiver bitmap with modern blend mode
    canvas.drawBitmap(this, offsetX, offsetY, paint)
    return combined
}


/**
 * Creates a copy of the bitmap with all pixels matching a specified color (within a given tolerance)
 * made transparent.
 *
 * This is useful for removing solid backgrounds like white or any flat color.
 *
 * @receiver The source [Bitmap] to process.
 * @param colorToRemove The target color to remove from the bitmap.
 * @param tolerance The allowed deviation per color channel when comparing with [colorToRemove].
 *                  Defaults to `10`. Must be in the range 0..255.
 * @return A new [Bitmap] with the background pixels made transparent.
 *
 * @see Color.TRANSPARENT
 *
 * @since 3.0.0
 */
fun Bitmap.removeBackground(colorToRemove: Int, tolerance: Int = 10): Bitmap {
    val output = copy(Bitmap.Config.ARGB_8888, true)

    for (x in 0 until width) {
        for (y in 0 until height) {
            val pixel = this[x, y]
            if (isSimilarColor(pixel, colorToRemove, tolerance)) {
                output[x, y] = Color.TRANSPARENT
            }
        }
    }
    return output
}


/**
 * Returns a new [Bitmap] tinted with the specified [color].
 *
 * This function applies a color filter over the original bitmap, replacing its colors
 * with the given [color], preserving the bitmap's alpha channel.
 *
 * @receiver The original bitmap to be tinted.
 * @param color The color to apply as a tint. Must be a valid color int (use [ColorInt]).
 * @return A new [Bitmap] tinted with the specified [color].
 *
 * @throws IllegalStateException if the bitmap is recycled.
 *
 * @since 3.0.0
 */
fun Bitmap.tintWithColor(@ColorInt color: Int): Bitmap {
    check(!isRecycled) { "Cannot tint a recycled Bitmap." }

    val result = createBitmap(width, height)
    val canvas = Canvas(result)
    canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR)
    val paint = Paint().apply {
        isAntiAlias = true
        colorFilter = PorterDuffColorFilter(color, Mode.SRC_IN)
    }
    canvas.drawBitmap(this, 0f, 0f, paint)
    return result
}






private fun calculateCoordinates(watermarkText: String, paint: Paint, options: WatermarkOptions, width: Int, height: Int, padding: Float): PointF {
    val x = when (options.corner) {
        Corner.TOP_LEFT,
        Corner.BOTTOM_LEFT -> {
            padding
        }

        Corner.TOP_RIGHT,
        Corner.BOTTOM_RIGHT -> {
            width - padding
        }
    }
    val y = when (options.corner) {
        Corner.BOTTOM_LEFT,
        Corner.BOTTOM_RIGHT -> {
            height - padding
        }

        Corner.TOP_LEFT,
        Corner.TOP_RIGHT -> {
            val bounds = Rect()
            paint.getTextBounds(watermarkText, 0, watermarkText.length, bounds)
            val textHeight = bounds.height()
            textHeight + padding

        }
    }
    return PointF(x, y)
}


private fun isSimilarColor(color1: Int, color2: Int, tolerance: Int): Boolean {
    val r1 = Color.red(color1)
    val g1 = Color.green(color1)
    val b1 = Color.blue(color1)

    val r2 = Color.red(color2)
    val g2 = Color.green(color2)
    val b2 = Color.blue(color2)

    return (abs(r1 - r2) <= tolerance && abs(g1 - g2) <= tolerance && abs(b1 - b2) <= tolerance)
}
