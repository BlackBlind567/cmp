package com.hybrid.internet.data.model.response
import kotlinx.serialization.Serializable

@Serializable
data class SupportTicket (
    val id: Int,
    val ticket_id: String,
    val customer_id: Int,
    val alt_mobile: String,
    val alt_email: String,
    val category: String,
    val sub_category: String,
    val location: String,
    val created_at: String
)