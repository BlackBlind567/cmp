package com.hybrid.internet.core.notification

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class NotificationNavigationEvent {
    data class OpenTickets(val ticketId: String?) : NotificationNavigationEvent()
}

object NotificationNavigationBus {
    private val _event = MutableStateFlow<NotificationNavigationEvent?>(null)
    val event = _event.asStateFlow()

    fun openTickets(ticketId: String?) {
        _event.value = NotificationNavigationEvent.OpenTickets(ticketId)
    }

    fun clear() {
        _event.value = null
    }
}
