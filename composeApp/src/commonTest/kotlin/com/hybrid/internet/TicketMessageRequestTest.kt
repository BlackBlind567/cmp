package com.hybrid.internet

import com.hybrid.internet.core.base.encodeImageDataUri
import com.hybrid.internet.data.model.request.SendTicketMessageRequest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class TicketMessageRequestTest {
    private val json = Json { explicitNulls = false }

    @Test
    fun photoMessageUsesImageKeyWithDataUriPrefix() {
        val payload = json.parseToJsonElement(json.encodeToString(
            SendTicketMessageRequest("ISP-2026-00001", "Router photo", encodeImageDataUri(byteArrayOf(0xff.toByte(), 0xd8.toByte(), 0xff.toByte())))
        )).jsonObject
        assertEquals("ISP-2026-00001", payload["ticket_id"]?.jsonPrimitive?.content)
        assertEquals("data:image/jpeg;base64,/9j/", payload["image"]?.jsonPrimitive?.content)
        assertEquals("Router photo", payload["message"]?.jsonPrimitive?.content)
    }

    @Test
    fun imageOnlyMessageKeepsEmptyCaptionAndTextOnlyOmitsImage() {
        val imageOnly = json.parseToJsonElement(json.encodeToString(
            SendTicketMessageRequest("ISP-2026-00001", "", "data:image/jpeg;base64,/9j/")
        )).jsonObject
        assertEquals("", imageOnly["message"]?.jsonPrimitive?.content)
        assertEquals("data:image/jpeg;base64,/9j/", imageOnly["image"]?.jsonPrimitive?.content)
        val textOnly = json.parseToJsonElement(json.encodeToString(
            SendTicketMessageRequest("ISP-2026-00001", "Hello")
        )).jsonObject
        assertFalse(textOnly.containsKey("image"))
    }
}
