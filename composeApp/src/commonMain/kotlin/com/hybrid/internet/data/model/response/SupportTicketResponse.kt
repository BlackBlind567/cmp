package com.hybrid.internet.data.model.response

import kotlinx.serialization.Serializable


@Serializable

data class SupportTicketResponse (
    val ticket: SupportTicket,
    val history: SupportTicketHistory
)