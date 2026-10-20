package com.hybrid.internet

import com.hybrid.internet.core.network.ApiResponse
import com.hybrid.internet.data.model.response.TicketMessageItem
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TicketMessageHistoryTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun historyDecodesPhotoAndTextOnlyMessagesTogether() {
        val response = json.decodeFromString<ApiResponse<JsonObject>>(
            """{"status":1,"success":true,"data":{"ticket_id":"ISP-2026-00001",
                "customer":{"name":"Customer"},"messages":[
                {"id":"photo-message","sender":"customer","sender_name":"Customer",
                 "text":"Router photo","attachment":{"mime_type":"image\/jpeg",
                 "image":"https:\/\/support.hybridisp.com\/api\/files\/photo?exp=123&sig=test"},
                 "created_at":"15\/10\/2026 06:42 PM"},
                {"id":"text-message","sender":"customer","text":"hi team","attachment":null}
                ]},"message":"Messages retrieved successfully"}"""
        )
        val messages = json.decodeFromJsonElement<List<TicketMessageItem>>(response.data!!.getValue("messages"))
        assertEquals(2, messages.size)
        assertEquals("Router photo", messages[0].message)
        assertEquals("customer", messages[0].senderType)
        assertEquals("https://support.hybridisp.com/api/files/photo?exp=123&sig=test", messages[0].image)
        assertEquals("hi team", messages[1].message)
        assertNull(messages[1].image)
    }

    @Test
    fun legacyAttachmentStringAndMissingImageAreSupported() {
        assertEquals("https://example.com/photo.jpg", json.decodeFromString<TicketMessageItem>(
            """{"attachment":"https://example.com/photo.jpg"}"""
        ).image)
        assertNull(json.decodeFromString<TicketMessageItem>("""{"attachment":{"mime_type":"image/jpeg"}}""").image)
        assertNull(json.decodeFromString<TicketMessageItem>("""{"text":"Hello"}""").image)
    }
}
