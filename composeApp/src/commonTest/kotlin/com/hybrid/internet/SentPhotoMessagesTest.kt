package com.hybrid.internet

import com.hybrid.internet.data.model.response.TicketMessageItem
import com.hybrid.internet.presentation.features.ticketMessages.SentPhotoMessages
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class SentPhotoMessagesTest {
    @Test
    fun photoAppearsImmediatelyAndSurvivesStaleHistory() {
        val photos = SentPhotoMessages()
        val bytes = byteArrayOf(1, 2, 3)
        assertSame(bytes, photos.add("ticket", "", bytes).single().localImage)
        assertSame(bytes, photos.update(emptyList()).single().localImage)
    }

    @Test
    fun serverEchoDoesNotDuplicateImageOnlyMessages() {
        val photos = SentPhotoMessages()
        val first = byteArrayOf(1)
        val second = byteArrayOf(2)
        photos.add("ticket", "", first)
        photos.add("ticket", "", second)
        val echo = TicketMessageItem(legacyMessage = "", legacySenderType = "customer")
        val merged = photos.update(listOf(echo, echo))
        assertEquals(2, merged.size)
        assertSame(first, merged[0].localImage)
        assertSame(second, merged[1].localImage)
    }

    @Test
    fun oldCaptionMatchIsNotReplaced() {
        val photos = SentPhotoMessages()
        val old = TicketMessageItem(legacyMessage = "Router", legacySenderType = "customer")
        photos.update(listOf(old))
        val bytes = byteArrayOf(3)
        photos.add("ticket", "Router", bytes)
        val merged = photos.update(listOf(old, old))
        assertEquals(null, merged[0].localImage)
        assertSame(bytes, merged[1].localImage)
    }

    @Test
    fun emptyImageUrlFallsBackToImageField() {
        assertEquals("photo.png", TicketMessageItem(imageUrl = "", legacyImage = "photo.png").image)
    }
}
