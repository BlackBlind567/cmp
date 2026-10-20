package com.hybrid.internet.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketConfirmationRequest(
    @SerialName("ticket_id") val ticketId: String,
    val action: String,
    val rating: Int? = null,
    val remark: String
) {
    init {
        require(ticketId.isNotBlank())
        require(action == "accept" || action == "reject")
        require(if (action == "accept") rating != null && rating in 1..5 else rating == null)
    }
}
