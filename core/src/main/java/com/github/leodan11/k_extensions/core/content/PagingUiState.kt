package com.github.leodan11.k_extensions.core.content

/**
 * Represents a simplified UI state derived from Paging 3 load states.
 *
 * @property isLoading indicates whether the initial load is in progress.
 * @property isEmpty indicates whether the dataset is empty.
 * @property error the current error if one exists.
 *
 * @since 3.0.1
 */
data class PagingUiState(val isLoading: Boolean, val isEmpty: Boolean, val error: Throwable?)
