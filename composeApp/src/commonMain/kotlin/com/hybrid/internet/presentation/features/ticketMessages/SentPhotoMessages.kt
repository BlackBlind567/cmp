package com.hybrid.internet.presentation.features.ticketMessages

import com.hybrid.internet.data.model.response.TicketMessageItem

/** Keeps successful photo sends visible while the server's history catches up. */
internal class SentPhotoMessages {
    private data class SentPhoto(val item: TicketMessageItem, val occurrence: Int)
    private val photos = mutableListOf<SentPhoto>()
    private var serverMessages = emptyList<TicketMessageItem>()

    fun add(ticketId: String, message: String, bytes: ByteArray): List<TicketMessageItem> {
        val occurrence = merged().count { it.isCustomerMessage(message) }
        photos += SentPhoto(
            TicketMessageItem(ticketId = ticketId, legacyMessage = message,
                legacySenderType = "customer", localImage = bytes,
                localKey = "local-photo-${photos.size}"), occurrence
        )
        return merged()
    }

    fun update(messages: List<TicketMessageItem>): List<TicketMessageItem> {
        val merged = serverMessages.toMutableList()
        messages.forEachIndexed { position, incoming ->
            val index = if (incoming.id != null) merged.indexOfFirst { it.id == incoming.id }
                else position.takeIf { it < merged.size && merged[it].id == null } ?: -1
            if (index < 0) merged += incoming
            else {
                val previous = merged[index]
                // Reuse identical rows; refresh signed URLs without replacing the thumbnail state.
                val sameContent = previous.message == incoming.message &&
                    previous.senderType == incoming.senderType && previous.senderName == incoming.senderName &&
                    previous.createdAt == incoming.createdAt && previous.status == incoming.status &&
                    previous.image == incoming.image
                if (!sameContent) merged[index] = incoming
            }
        }
        serverMessages = merged
        return merged()
    }

    private fun merged(): List<TicketMessageItem> {
        val result = serverMessages.toMutableList()
        for (photo in photos) {
            // Match the occurrence, not just caption, so repeated/empty captions remain distinct.
            val index = serverMessages.indices.filter {
                serverMessages[it].isCustomerMessage(photo.item.message.orEmpty())
            }.getOrNull(photo.occurrence)
            if (index != null) {
                result[index] = result[index].copy(localImage = photo.item.localImage, localKey = photo.item.localKey)
            } else {
                result += photo.item
            }
        }
        return result
    }

    private fun TicketMessageItem.isCustomerMessage(caption: String): Boolean =
        (senderType.equals("customer", true) || senderType.equals("user", true)) &&
            message.orEmpty().trim() == caption
}
