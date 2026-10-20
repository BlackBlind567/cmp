package com.hybrid.internet

import com.hybrid.internet.presentation.features.dashboard.TabRefreshController
import kotlin.test.Test
import kotlin.test.assertEquals

class TabRefreshTest {
    @Test
    fun firstVisitUsesInitialLoadAndRepeatClicksRefresh() {
        val controller = TabRefreshController()
        var refreshes = 0
        controller.request("plans")
        controller.register("plans") { refreshes++ }
        assertEquals(0, refreshes)
        controller.request("plans")
        controller.request("plans")
        assertEquals(2, refreshes)
    }

    @Test
    fun returningTabReceivesPendingRefreshExactlyOnce() {
        val controller = TabRefreshController()
        var refreshes = 0
        controller.register("tickets") { refreshes++ }
        controller.unregister("tickets")
        controller.request("tickets")
        controller.register("tickets") { refreshes++ }
        assertEquals(1, refreshes)
        controller.unregister("tickets")
        controller.register("tickets") { refreshes++ }
        assertEquals(1, refreshes)
    }

    @Test
    fun selectingAnotherTabDoesNotRefreshCurrentTab() {
        val controller = TabRefreshController()
        var homeRefreshes = 0
        controller.register("home") { homeRefreshes++ }
        controller.request("services")
        assertEquals(0, homeRefreshes)
    }
}
