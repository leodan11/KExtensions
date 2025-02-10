package com.github.leodan11.k_extensions.lifecycle

import androidx.annotation.CheckResult
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Repeats the given [block] on the given [state] of the [Lifecycle] of this [Fragment].
 *
 * The [block] will be invoked on the [LifecycleOwner]'s [Lifecycle] whenever the given [state]
 * is reached.
 *
 * The [block] will be invoked immediately if the [Lifecycle] is in the given [state] when this
 * function is called.
 *
 * The [block] will be invoked on a background thread.
 */
fun Fragment.repeatingJobOn(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(state, block)
    }
}


/**
 * Repeats the given [block] on the given [Lifecycle.State.STARTED] of the [Lifecycle] of this [Fragment].
 *
 * The [block] will be invoked on the [LifecycleOwner]'s [Lifecycle] whenever the given [Lifecycle.State.STARTED]
 * is reached.
 *
 * The [block] will be invoked immediately if the [Lifecycle] is in the given [Lifecycle.State.STARTED] when this
 * function is called.
 *
 * The [block] will be invoked on a background thread.
 */
fun Fragment.repeatingJobOnStarted(block: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED, block)
    }
}


/**
 * Repeats the given [block] on the given [Lifecycle.State.RESUMED] of the [Lifecycle] of this [Fragment].
 *
 * The [block] will be invoked on the [LifecycleOwner]'s [Lifecycle] whenever the given [Lifecycle.State.RESUMED]
 * is reached.
 *
 * The [block] will be invoked immediately if the [Lifecycle] is in the given [Lifecycle.State.RESUMED] when this
 * function is called.
 *
 * The [block] will be invoked on a background thread.
 */
fun Fragment.repeatingJobOnResumed(block: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED, block)
    }
}


/**
 * Repeats the given [block] on the given [state] of the [Lifecycle] of this [Fragment].
 *
 * The [block] will be invoked on the [LifecycleOwner]'s [Lifecycle] whenever the given [state]
 * is reached.
 *
 * The [block] will be invoked immediately if the [Lifecycle] is in the given [state] when this
 * function is called.
 *
 * The [block] will be invoked on a background thread.
 */
fun AppCompatActivity.repeatingJobOn(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(state, block)
    }
}


/**
 * Repeats the given [block] on the given [Lifecycle.State.STARTED] of the [Lifecycle] of this [Fragment].
 *
 * The [block] will be invoked on the [LifecycleOwner]'s [Lifecycle] whenever the given [Lifecycle.State.STARTED]
 * is reached.
 *
 * The [block] will be invoked immediately if the [Lifecycle] is in the given [Lifecycle.State.STARTED] when this
 * function is called.
 *
 * The [block] will be invoked on a background thread.
 */
fun AppCompatActivity.repeatingJobOnStarted(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED, block)
    }
}


/**
 * Repeats the given [block] on the given [Lifecycle.State.RESUMED] of the [Lifecycle] of this [Fragment].
 *
 * The [block] will be invoked on the [LifecycleOwner]'s [Lifecycle] whenever the given [Lifecycle.State.RESUMED]
 * is reached.
 *
 * The [block] will be invoked immediately if the [Lifecycle] is in the given [Lifecycle.State.RESUMED] when this
 * function is called.
 *
 * The [block] will be invoked on a background thread.
 */
fun AppCompatActivity.repeatingJobOnResumed(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch { repeatOnLifecycle(Lifecycle.State.RESUMED, block) }
}


/**
 * Add a [LifecycleEventObserver] that will be invoked on the given [Lifecycle.Event] of the [Lifecycle] of this [Fragment].
 *
 * @receiver [Lifecycle]
 *
 * @param which [Lifecycle.Event] to listen
 *
 * @param block [() -> Unit] to be invoked
 *
 */
fun Lifecycle.doOnEvent(which: Lifecycle.Event, block: () -> Unit) {
    val observer = object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (which != event) return
            removeObserver(this)
            block()
        }
    }

    addObserver(observer)
}


/**
 * Returns the [CoroutineScope] for the [Lifecycle] of this [Fragment].
 */
val Fragment.viewCoroutineScope get() = viewLifecycleOwner.lifecycle.coroutineScope


/**
 * True if the lifecycles current state is at least [Lifecycle.State.CREATED].
 */
@get:CheckResult
inline val Lifecycle.isAtLeastCreated get() = currentState.isAtLeast(Lifecycle.State.CREATED)


/**
 * True if the lifecycles current state is at least [Lifecycle.State.STARTED].
 */
@get:CheckResult
inline val Lifecycle.isAtLeastStarted get() = currentState.isAtLeast(Lifecycle.State.STARTED)


/**
 * True if the lifecycles current state is [Lifecycle.State.RESUMED].
 */
@get:CheckResult
inline val Lifecycle.isResumed get() = currentState === Lifecycle.State.RESUMED