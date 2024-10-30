package com.github.leodan11.k_extensions.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

/**
 * Create a bitmap from a view
 *
 * @receiver [View]
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
