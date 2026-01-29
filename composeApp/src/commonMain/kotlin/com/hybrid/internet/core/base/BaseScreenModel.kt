package com.hybrid.internet.core.base

import cafe.adriel.voyager.core.model.ScreenModel
import com.hybrid.internet.core.state.UiEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseScreenModel : ScreenModel {

    protected val screenModelScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate
    )

    protected val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    protected suspend fun sendEvent(event: UiEvent) {
        _events.emit(event)
    }

    override fun onDispose() {
        screenModelScope.cancel()
    }
}