package com.business.cmpproject.core.state

sealed class UiEvent {
    data class ShowSnackBar(
        val message: String,
        val isError: Boolean = true
    ) : UiEvent()
}