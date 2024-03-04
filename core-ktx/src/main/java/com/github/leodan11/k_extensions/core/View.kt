package com.github.leodan11.k_extensions.core

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
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
    } else if (d is AnimatedVectorDrawable) {
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