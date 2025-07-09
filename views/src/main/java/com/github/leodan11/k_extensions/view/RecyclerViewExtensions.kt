package com.github.leodan11.k_extensions.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Extension function for `RecyclerView` that controls its visibility based on whether it has items
 * in its adapter.
 *
 * If the `RecyclerView` has no items (i.e., the `ListAdapter` is empty), the visibility of the
 * `RecyclerView` is set to `View.GONE`. If the `RecyclerView` has items, its visibility is set
 * to `View.VISIBLE`.
 *
 * This is useful when you want to hide an empty `RecyclerView` in the UI to avoid displaying
 * an unnecessary empty container.
 *
 * @receiver RecyclerView The `RecyclerView` on which this extension function is called.
 */
fun RecyclerView.toggleVisibilityBasedOnItems(vararg extras: View) {
    val itemCount = this.adapter?.itemCount ?: 0
    val visibilityState = if (itemCount == 0) View.GONE else View.VISIBLE
    this.visibility = visibilityState
    extras.forEach { it.visibility = visibilityState }
}