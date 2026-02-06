package com.hybrid.internet.core.session

import com.hybrid.internet.core.network.HttpError
import com.hybrid.internet.core.state.UiEvent
import com.hybrid.internet.core.state.UiEventBus
import com.hybrid.internet.core.storage.LocalStorage

class SessionManager(
    private val storage: LocalStorage,
    private val uiEventBus: UiEventBus
) {

    fun handle(error: HttpError): Boolean {
        return if (error is HttpError.Unauthorized) {
            println("Unauthorized Triggered")
            storage.clear()
            uiEventBus.send(UiEvent.Unauthorized)
            true // session expired
        } else {
            false
        }
    }
}