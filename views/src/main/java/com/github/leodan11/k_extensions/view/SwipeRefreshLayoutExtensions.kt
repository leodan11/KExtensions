package com.github.leodan11.k_extensions.view

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * Set the progress drawable
 *
 * @receiver [SwipeRefreshLayout]
 *
 * @param resId [Int] ID of the drawable
 * @param paddingLeft [Int] Default is `10`
 * @param paddingTop [Int] Default is `10`
 * @param paddingRight [Int] Default is `10`
 * @param paddingBottom [Int] Default is `10`
 *
 *
 */
fun SwipeRefreshLayout.setProgressImageResource(
    @DrawableRes resId: Int,
    paddingLeft: Int = 10,
    paddingTop: Int = 10,
    paddingRight: Int = 10,
    paddingBottom: Int = 10
) {
    val field = SwipeRefreshLayout::class.java.getDeclaredField("mCircleView")
    field.isAccessible = true
    val imageView = field.get(this) as ImageView
    imageView.setImageResource(resId)
    imageView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
}