package com.github.leodan11.k_extensions.view

import android.os.Build
import android.view.View
import android.view.View.OnScrollChangeListener
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton


/**
 * Extension function for [FloatingActionButton] that adjusts its visibility based on the scroll
 * behavior of a provided view. This function supports common scrolling views such as
 * [RecyclerView], [NestedScrollView], and [ListView].
 *
 * It hides the [FloatingActionButton] when the user scrolls down and shows it when the user
 * scrolls up.
 *
 * @param view The scrollable view to observe. It can be of type [RecyclerView], [NestedScrollView],
 *             or [ListView]. Depending on the view type, the FAB behavior is handled accordingly.
 */
@RequiresApi(Build.VERSION_CODES.M)
fun FloatingActionButton.setupScrollBehavior(view: View) {
    // Variable to track whether the FAB should be hidden or visible
    var isScrolledDown = false

    /**
     * Updates the visibility of the [FloatingActionButton] based on the scroll direction.
     *
     * @param isScrollingDown If the user is scrolling down. If `true`, the FAB will be hidden;
     *                        if `false`, the FAB will be shown.
     */
    fun updateFabVisibility(isScrollingDown: Boolean) {
        if (isScrollingDown && !isScrolledDown) {
            this.hide() // Hide the FAB when scrolling down
            isScrolledDown = true
        } else if (!isScrollingDown && isScrolledDown) {
            this.show() // Show the FAB when scrolling up
            isScrolledDown = false
        }
    }

    // Identifying the type of view and setting the appropriate scroll listener
    when (view) {
        /**
         * Case for [RecyclerView]. It uses an [RecyclerView.OnScrollListener] to detect scroll events.
         */
        is RecyclerView -> {
            view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                /**
                 * Called whenever the [RecyclerView] is scrolled.
                 *
                 * @param recyclerView The [RecyclerView] that is being scrolled.
                 * @param dx Horizontal scroll amount (not used in this case).
                 * @param dy Vertical scroll amount. A positive value indicates scrolling down,
                 *           and a negative value indicates scrolling up.
                 */
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    updateFabVisibility(dy > 0) // Hide FAB when scrolling down
                }
            })
        }
        /**
         * Case for [NestedScrollView]. It uses the [OnScrollChangeListener] to detect scroll events.
         */
        is NestedScrollView -> {
            view.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                updateFabVisibility(scrollY > oldScrollY) // Hide FAB when scrolling down
            }
        }
        /**
         * Case for [ListView]. It uses an [AbsListView.OnScrollListener] to detect scroll events.
         */
        is ListView -> {
            view.setOnScrollListener(object : AbsListView.OnScrollListener {
                /**
                 * This method is not used directly, as visibility control is handled
                 * through the scroll state.
                 */
                override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) = Unit

                /**
                 * Called when the scroll state changes.
                 * In this case, we are interested in whether the user is scrolling or if the scroll is idle.
                 *
                 * @param view The [AbsListView] being scrolled.
                 * @param scrollState The current scroll state.
                 *                    [SCROLL_STATE_TOUCH_SCROLL] indicates the user is scrolling,
                 *                    [SCROLL_STATE_IDLE] indicates scrolling has stopped.
                 */
                override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                    updateFabVisibility(scrollState == SCROLL_STATE_TOUCH_SCROLL) // Hide FAB if scrolling
                }
            })
        }
    }
}


/**
 * Extension function for [ExtendedFloatingActionButton] that adjusts its visibility based on
 * the scroll behavior of a provided view. This function supports common scrollable views such as
 * [RecyclerView], [NestedScrollView], and [ListView].
 *
 * It hides the [ExtendedFloatingActionButton] when the user scrolls down and shows it when the user
 * scrolls up.
 *
 * @param view The scrollable view to observe. It can be of type [RecyclerView], [NestedScrollView],
 *             or [ListView]. Depending on the view type, the FAB behavior is handled accordingly.
 */
@RequiresApi(Build.VERSION_CODES.M)
fun ExtendedFloatingActionButton.setupScrollBehavior(view: View) {
    // Variable to track whether the FAB should be hidden or visible
    var isScrolledDown = false

    /**
     * Updates the visibility of the [ExtendedFloatingActionButton] based on the scroll direction.
     *
     * @param isScrollingDown If the user is scrolling down. If `true`, the FAB will be hidden;
     *                        if `false`, the FAB will be shown.
     */
    fun updateFabVisibility(isScrollingDown: Boolean) {
        if (isScrollingDown && !isScrolledDown) {
            this.hide() // Hide the FAB when scrolling down
            isScrolledDown = true
        } else if (!isScrollingDown && isScrolledDown) {
            this.show() // Show the FAB when scrolling up
            isScrolledDown = false
        }
    }

    // Identifying the type of view and setting the appropriate scroll listener
    when (view) {
        /**
         * Case for [RecyclerView]. It uses an [RecyclerView.OnScrollListener] to detect scroll events.
         */
        is RecyclerView -> {
            view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                /**
                 * Called whenever the [RecyclerView] is scrolled.
                 *
                 * @param recyclerView The [RecyclerView] that is being scrolled.
                 * @param dx Horizontal scroll amount (not used in this case).
                 * @param dy Vertical scroll amount. A positive value indicates scrolling down,
                 *           and a negative value indicates scrolling up.
                 */
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    updateFabVisibility(dy > 0) // Hide FAB when scrolling down
                }
            })
        }
        /**
         * Case for [NestedScrollView]. It uses the [OnScrollChangeListener] to detect scroll events.
         */
        is NestedScrollView -> {
            view.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                updateFabVisibility(scrollY > oldScrollY) // Hide FAB when scrolling down
            }
        }
        /**
         * Case for [ListView]. It uses an [AbsListView.OnScrollListener] to detect scroll events.
         */
        is ListView -> {
            view.setOnScrollListener(object : AbsListView.OnScrollListener {
                /**
                 * This method is not used directly, as visibility control is handled
                 * through the scroll state.
                 */
                override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) = Unit

                /**
                 * Called when the scroll state changes.
                 * In this case, we are interested in whether the user is scrolling or if the scroll is idle.
                 *
                 * @param view The [AbsListView] being scrolled.
                 * @param scrollState The current scroll state.
                 *                    [SCROLL_STATE_TOUCH_SCROLL] indicates the user is scrolling,
                 *                    [SCROLL_STATE_IDLE] indicates scrolling has stopped.
                 */
                override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                    updateFabVisibility(scrollState == SCROLL_STATE_TOUCH_SCROLL) // Hide FAB if scrolling
                }
            })
        }
    }
}