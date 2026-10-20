package com.hybrid.internet.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SupportTicketHistory (
    val id: Int? = null,
    @SerialName("ticket_id")
    val ticketId: Int? = null,
    @SerialName("customer_id")
    val customerId: Int? = null,
    val image: String? = null,
    val message: String? = null,
    val remark: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)
