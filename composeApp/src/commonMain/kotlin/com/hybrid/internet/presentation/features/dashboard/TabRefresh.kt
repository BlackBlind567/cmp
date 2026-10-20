package com.hybrid.internet.presentation.features.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.staticCompositionLocalOf

internal class TabRefreshController {
    private val callbacks = mutableMapOf<String, () -> Unit>()
    private val visited = mutableSetOf<String>()
    private val pending = mutableSetOf<String>()

    fun request(key: String) {
        val callback = callbacks[key]
        if (callback != null) callback() else pending.add(key)
    }

    fun register(key: String, callback: () -> Unit) {
        callbacks[key] = callback
        val returning = !visited.add(key)
        val requested = pending.remove(key)
        // First entry already loads in the ScreenModel's init block.
        if (returning && requested) callback()
    }

    fun unregister(key: String) { callbacks.remove(key) }
}

internal val LocalTabRefresh = staticCompositionLocalOf<TabRefreshController?> { null }

@Composable
internal fun RegisterTabRefresh(key: String, model: Any, refresh: () -> Unit) {
    val controller = LocalTabRefresh.current
    DisposableEffect(controller, key, model) {
        controller?.register(key, refresh)
        onDispose { controller?.unregister(key) }
    }
}
