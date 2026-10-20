package com.hybrid.internet

import com.hybrid.internet.core.network.ApiResponse
import com.hybrid.internet.data.model.request.TicketConfirmationRequest
import com.hybrid.internet.data.model.response.TicketConfirmationResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TicketConfirmationTest {
    private val json = Json { ignoreUnknownKeys = true; explicitNulls = false }

    @Test
    fun acceptanceIncludesNumericRatingAndOptionalEmptyRemark() {
        val payload = json.parseToJsonElement(json.encodeToString(
            TicketConfirmationRequest("ISP-2026-00001", "accept", 4, "")
        )).jsonObject
        assertEquals("ISP-2026-00001", payload["ticket_id"]?.jsonPrimitive?.content)
        assertEquals("accept", payload["action"]?.jsonPrimitive?.content)
        assertEquals("4", payload["rating"].toString())
        assertEquals("", payload["remark"]?.jsonPrimitive?.content)
    }

    @Test
    fun rejectionOmitsRating() {
        val payload = json.parseToJsonElement(json.encodeToString(
            TicketConfirmationRequest("ISP-2026-00001", "reject", null, "Still offline")
        )).jsonObject
        assertFalse(payload.containsKey("rating"))
        assertEquals("reject", payload["action"]?.jsonPrimitive?.content)
        assertEquals("Still offline", payload["remark"]?.jsonPrimitive?.content)
    }

    @Test
    fun invalidRatingsAndActionsAreRejected() {
        for (rating in listOf(null, 0, 6)) {
            assertFailsWith<IllegalArgumentException> {
                TicketConfirmationRequest("ISP-2026-00001", "accept", rating, "")
            }
        }
        assertFailsWith<IllegalArgumentException> {
            TicketConfirmationRequest("ISP-2026-00001", "reject", 4, "")
        }
        assertFailsWith<IllegalArgumentException> {
            TicketConfirmationRequest("ISP-2026-00001", "resolved", 4, "")
        }
    }

    @Test
    fun confirmationReadsServerStatusEvenWithLegacyEnvelopeTypo() {
        val response = json.decodeFromString<ApiResponse<TicketConfirmationResponse>>(
            """{"staus":1,"success":true,"data":{"ticket_id":"ISP-2026-00001","status":"Resolved"},"message":"Confirmed"}"""
        )
        assertTrue(response.success)
        assertEquals("Resolved", response.data?.status)
    }
}
