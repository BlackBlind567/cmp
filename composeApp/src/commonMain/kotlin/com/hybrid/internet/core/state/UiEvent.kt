package com.hybrid.internet.core.state

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed class UiEvent {
    data class ShowSnackBar(
        val message: String,
        val isError: Boolean = true
    ) : UiEvent()
    object NavigateBack : UiEvent()   // 👈 ADD THIS

    object RefreshPrevious : UiEvent()   // 👈 ADD THIS
    object NavigateToLogin : UiEvent() // 👈 ADD THIS

    object Unauthorized : UiEvent()
}

class UiEventBus {
    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun send(event: UiEvent) {
        _events.tryEmit(event)
    }
}