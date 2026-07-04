package com.github.leodan11.k_extensions.lifecycle.components

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData


/**
 * A [MediatorLiveData] that combines the latest values from two source
 * [LiveData] instances into a single observable result.
 *
 * Whenever either source emits a new value, the provided [combine] function
 * is invoked with the latest values from both sources and its result is
 * emitted to observers.
 *
 * By default, emissions start only after both sources have emitted at least
 * once. This behavior can be changed by setting [requireBothSources] to `false`.
 *
 * This implementation correctly distinguishes between:
 * - A source that has not emitted yet.
 * - A source that has emitted a `null` value.
 *
 * Duplicate emissions are ignored when the newly combined value is equal
 * to the current value.
 *
 * @param A Type of the first source LiveData.
 * @param B Type of the second source LiveData.
 * @param R Type of the emitted result.
 * @param sourceA First LiveData source.
 * @param sourceB Second LiveData source.
 * @param requireBothSources If `true`, no value is emitted until both
 * sources have emitted at least once. Default is `true`.
 * @param combine Function used to combine the latest values from both
 * sources into a result of type [R].
 *
 * Example:
 * ```kotlin
 * val userState = DoubleTriggerMediatorLiveData(
 *     sourceA = userLiveData,
 *     sourceB = settingsLiveData
 * ) { user, settings ->
 *     UserUiState(user = user, settings = settings)
 * }
 * ```
 * @since 2.2.1
 */
class DoubleTriggerMediatorLiveData<A, B, R>(sourceA: LiveData<A>, sourceB: LiveData<B>, private val requireBothSources: Boolean = true, private val combine: (A?, B?) -> R) : MediatorLiveData<R>() {

    private var latestA: A? = null
    private var latestB: B? = null

    private var hasSourceAEmitted = false
    private var hasSourceBEmitted = false

    init {
        addSource(sourceA) { valueA ->
            latestA = valueA
            hasSourceAEmitted = true
            emitCombinedValue()
        }

        addSource(sourceB) { valueB ->
            latestB = valueB
            hasSourceBEmitted = true
            emitCombinedValue()
        }
    }

    private fun emitCombinedValue() {
        if (requireBothSources && (!hasSourceAEmitted || !hasSourceBEmitted) ) {
            return
        }

        val combinedValue = combine(latestA, latestB)

        if (value != combinedValue) {
            value = combinedValue
        }
    }
}
