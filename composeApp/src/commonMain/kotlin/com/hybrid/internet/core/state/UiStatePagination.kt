package com.hybrid.internet.core.state

sealed class UiStatePagination<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()

    data class Success<T>(
        val data: T,
        val isLoadingMore: Boolean = false,
        val canLoadMore: Boolean = true
    ) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
