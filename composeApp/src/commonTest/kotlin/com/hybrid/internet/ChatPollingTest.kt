package com.hybrid.internet

import com.hybrid.internet.data.model.response.TicketMessageItem
import com.hybrid.internet.presentation.features.ticketMessages.SentPhotoMessages
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class ChatPollingTest {
    private fun message(id: String, text: String = "Hello") =
        Json.decodeFromString<TicketMessageItem>(
            """{"id":"$id","sender":"customer","text":"$text"}"""
        )

    @Test
    fun unchangedPollReusesExistingMessages() {
        val history = SentPhotoMessages()
        val first = history.update(listOf(message("1")))
        val next = history.update(listOf(message("1")))
        assertEquals(first, next)
        assertSame(first[0], next[0])
    }

    @Test
    fun newMessagesAppendWithoutDuplicatingOrRemovingOlderRows() {
        val history = SentPhotoMessages()
        val first = history.update(listOf(message("1")))
        val next = history.update(listOf(message("1"), message("2")))
        assertEquals(listOf("1", "2"), next.map { it.id })
        assertSame(first[0], next[0])
        assertEquals(next, history.update(listOf(message("2"))))
    }

    @Test
    fun changedMessageUpdatesInPlace() {
        val history = SentPhotoMessages()
        history.update(listOf(message("1"), message("2")))
        val next = history.update(listOf(message("1", "Updated"), message("2")))
        assertEquals(2, next.size)
        assertEquals("Updated", next[0].message)
    }
}
