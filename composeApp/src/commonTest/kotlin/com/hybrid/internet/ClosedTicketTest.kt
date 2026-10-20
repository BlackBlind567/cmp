package com.hybrid.internet

import com.hybrid.internet.domain.repository.ticket.closedTicketError
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ClosedTicketTest {
    @Test
    fun closedTicketResponseUsesDetailedReason() {
        val body = Json.parseToJsonElement("""{"status":2,"success":false,"message":"Message not sent","data":"This ticket is closed. Please raise a new ticket for further assistance."}""").jsonObject
        assertEquals("This ticket is closed. Please raise a new ticket for further assistance.", closedTicketError(body)?.message)
    }

    @Test
    fun ordinaryFailuresDoNotCloseChat() {
        val body = Json.parseToJsonElement("""{"status":2,"success":false,"message":"Message not sent","data":"Please try again."}""").jsonObject
        assertNull(closedTicketError(body))
        assertNull(closedTicketError(null))
    }

    @Test
    fun successfulMessagesDoNotCloseChat() {
        val body = Json.parseToJsonElement("""{"success":true,"data":"This ticket is closed"}""").jsonObject
        assertNull(closedTicketError(body))
    }
}
