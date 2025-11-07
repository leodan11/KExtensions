package com.github.leodan11.k_extensions.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData


/**
 * A [MediatorLiveData] that combines two source [LiveData] objects into one.
 *
 * By default, it emits a [Pair] of the last values from both sources, but
 * you can provide a custom [combine] lambda to produce any type [R].
 *
 * It only emits when both sources have values unless [emitIfNull] is set to true.
 *
 * @param A Type of the first LiveData
 * @param B Type of the second LiveData
 * @param R Type of the output LiveData
 * @param a The first LiveData source
 * @param b The second LiveData source
 * @param emitIfNull If true, emits values even if one of the sources is null (default: false)
 * @param combine Function that takes the last values of A and B and returns R
 *
 * ```kotlin
 *
 * fun exampleUsage(a: LiveData<Int>, b: LiveData<String>): DoubleTriggerMediatorLiveData<Int, String, Pair<Int?, String?>> {
 *     return DoubleTriggerMediatorLiveData(a, b) { first, second -> first to second }
 * }
 *
 * ```
 * @since 2.2.1
 */
class DoubleTriggerMediatorLiveData<A, B, R>(
    a: LiveData<A>,
    b: LiveData<B>,
    private val emitIfNull: Boolean = false,
    private val combine: (A?, B?) -> R
) : MediatorLiveData<R>() {

    private var lastA: A? = null
    private var lastB: B? = null

    init {
        addSource(a) {
            lastA = it
            emitIfNeeded()
        }
        addSource(b) {
            lastB = it
            emitIfNeeded()
        }
    }

    private fun emitIfNeeded() {
        if (emitIfNull || (lastA != null && lastB != null)) {
            value = combine(lastA, lastB)
        }
    }

}
