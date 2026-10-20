package com.hybrid.internet.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendTicketMessageRequest(
    @SerialName("ticket_id")
    val ticketId: String,
    val message: String,
    val image: String? = null
)
