package com.hybrid.internet.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketHistoryItem(
    val id: Int? = null,
    @SerialName("ticket_id") val ticketId: Int? = null, // "?" lagao aur "= null" do
    val message: String? = null,
    val status: String? = null,
    @SerialName("customer_name") val customerName: String,
    @SerialName("company_name") val companyName: String,
    val image: String? = null
)