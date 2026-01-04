package com.business.cmpproject.core

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.business.cmpproject.core.state.UiEvent
import kotlinx.coroutines.*
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