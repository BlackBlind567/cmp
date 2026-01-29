package com.hybrid.internet.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ticket(
    val id: Int? = null,
    @SerialName("ticket_id") val ticketId: String? = null,
    @SerialName("customer_id") val customerId: Int? = null,
    @SerialName("alt_mobile") val altMobile: String? = null,
    @SerialName("alt_email") val altEmail: String? = null,
    val category: String? = null,
    val remark: String? = null,
    val del: Int? = null,
    val status: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)
