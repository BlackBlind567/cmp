package com.hybrid.internet.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketMessagesRequest(
    @SerialName("ticket_id")
    val ticketId: String
)
