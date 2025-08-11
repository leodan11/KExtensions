package com.github.leodan11.k_extensions.view

import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Connects this [TabLayout] with a [ViewPager2] using [TabLayoutMediator].
 * This version uses `autoRefresh = false` by default.
 *
 * Use this when your ViewPager's content is static and does not change dynamically.
 *
 * @param viewPager The [ViewPager2] to be linked with this TabLayout.
 * @param configureTab A lambda to configure each tab for a given position.
 * Receives the tab and its index as parameters.
 *
 * @return The [TabLayoutMediator] instance created and attached.
 */
fun TabLayout.linkWithViewPager(
    viewPager: ViewPager2,
    configureTab: (TabLayout.Tab, Int) -> Unit
): TabLayoutMediator {
    return this.linkWithViewPager(viewPager, autoRefresh = false, configureTab = configureTab)
}

/**
 * Connects this [TabLayout] with a [ViewPager2] using [TabLayoutMediator],
 * with control over the [autoRefresh] behavior.
 *
 * Use [autoRefresh] = `true` if your ViewPager's adapter updates dynamically
 * (e.g. pages added or removed after initial setup).
 *
 * @param viewPager The [ViewPager2] to be linked with this TabLayout.
 * @param autoRefresh Whether the TabLayout should update automatically when the adapter changes.
 * @param configureTab A lambda to configure each tab for a given position.
 * Receives the tab and its index as parameters.
 *
 * @return The [TabLayoutMediator] instance created and attached.
 */
fun TabLayout.linkWithViewPager(
    viewPager: ViewPager2,
    autoRefresh: Boolean,
    configureTab: (TabLayout.Tab, Int) -> Unit
): TabLayoutMediator {
    val mediator = TabLayoutMediator(this, viewPager, autoRefresh, configureTab)
    mediator.attach()
    return mediator
}