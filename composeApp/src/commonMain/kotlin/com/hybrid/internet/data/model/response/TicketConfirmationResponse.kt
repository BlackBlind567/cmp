package com.hybrid.internet.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketConfirmationResponse(
    @SerialName("ticket_id") val ticketId: String,
    val status: String
)
