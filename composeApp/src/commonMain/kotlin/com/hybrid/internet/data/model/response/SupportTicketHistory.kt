package com.hybrid.internet.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SupportTicketHistory (
    val id: Int,
    val ticket_id: Int,
    val customer_id: Int,
    val image: String,
    val message: String,
    val remark: String,
    val created_at: String
)