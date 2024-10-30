package com.github.leodan11.k_extensions.view

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.ImageView
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat

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
