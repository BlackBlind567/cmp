package com.hybrid.internet.core.state

sealed class UiEvent {
    data class ShowSnackBar(
        val message: String,
        val isError: Boolean = true
    ) : UiEvent()
    object NavigateBack : UiEvent()   // 👈 ADD THIS

    object RefreshPrevious : UiEvent()   // 👈 ADD THIS
}