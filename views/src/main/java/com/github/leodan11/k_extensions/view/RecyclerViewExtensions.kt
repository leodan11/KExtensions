package com.github.leodan11.k_extensions.view

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.MainThread
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

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

/**
 * Observes the [ListAdapter] data and automatically toggles the visibility of the provided
 * [RecyclerView]s and "empty" views based on whether the adapter has items.
 *
 * **Behavior:**
 * - When the adapter has items, the specified [RecyclerView]s are shown and the [emptyViews] are hidden.
 * - When the adapter is empty, the [RecyclerView]s are hidden and the [emptyViews] are shown.
 *
 * **Usage:**
 * ```
 * val adapter = MyListAdapter()
 * adapter.handleEmptyState(
 *     recyclerViews = arrayOf(recyclerView),
 *     emptyViews = arrayOf(emptyView)
 * )
 * ```
 *
 * @param recyclerViews The [RecyclerView]s that should be visible when the adapter has items.
 * @param emptyViews The views that should be visible when the adapter is empty.
 *
 * **Notes:**
 * - This extension uses [RecyclerView.AdapterDataObserver] internally to react to dataset changes.
 * - The visibility is updated immediately upon registration and whenever items are inserted, removed, or the dataset changes.
 *
 * @since 2.2.7
 */
@MainThread
fun <T : Any, VH : RecyclerView.ViewHolder> ListAdapter<T, VH>.handleEmptyState(
    recyclerViews: Array<out RecyclerView>,
    emptyViews: Array<out View>
) {
    val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            updateEmptyState()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            updateEmptyState()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            updateEmptyState()
        }

        private fun updateEmptyState() {
            val isEmpty = this@handleEmptyState.itemCount == 0
            recyclerViews.forEach { it.isVisible = !isEmpty }
            emptyViews.forEach { it.isVisible = isEmpty }
        }
    }
    this.registerAdapterDataObserver(observer)
    observer.onChanged()
}


/**
 * Notifies changes only for the visible items of a [RecyclerView] using a [ListAdapter].
 *
 * This extension is useful when only properties of visible items change (e.g., selection, click
 * enable/disable, progress) and you want to avoid refreshing the entire list, improving UI performance.
 *
 * <strong>Compatibility:</strong> Works with [LinearLayoutManager] and [GridLayoutManager].
 * For other [RecyclerView.LayoutManager] types, it will fall back to [ListAdapter.notifyDataSetChanged] as a last resort.
 *
 * <strong>Notes:</strong>
 * - [StaggeredGridLayoutManager] is not supported in this version.
 * - The RecyclerView must be fully initialized and attached to the adapter.
 *
 * @receiver [ListAdapter] containing the items.
 * @param recyclerView The [RecyclerView] whose visible items you want to update.
 *
 * @see ListAdapter.notifyItemRangeChanged
 * @see ListAdapter.notifyDataSetChanged
 *
 * @since 2.2.7
 */
@SuppressLint("NotifyDataSetChanged")
@MainThread
fun <T : Any, VH : RecyclerView.ViewHolder> ListAdapter<T, VH>.notifyVisibleItemsChanged(
    recyclerView: RecyclerView
) {
    val lm = recyclerView.layoutManager
    val firstVisible: Int
    val lastVisible: Int

    when (lm) {
        is GridLayoutManager -> {
            firstVisible = lm.findFirstVisibleItemPosition()
            lastVisible = lm.findLastVisibleItemPosition()
        }

        is LinearLayoutManager -> {
            firstVisible = lm.findFirstVisibleItemPosition()
            lastVisible = lm.findLastVisibleItemPosition()
        }

        else -> {
            notifyDataSetChanged()
            return
        }
    }

    if (firstVisible != RecyclerView.NO_POSITION && lastVisible != RecyclerView.NO_POSITION) {
        notifyItemRangeChanged(firstVisible, lastVisible - firstVisible + 1)
    }
}
