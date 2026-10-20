package com.hybrid.internet.data.model.response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SupportTicket (
    val id: Int? = null,
    @SerialName("ticket_id")
    val ticketId: String? = null,
    @SerialName("customer_id")
    val customerId: Int? = null,
    @SerialName("alt_mobile")
    val altMobile: String? = null,
    @SerialName("alt_email")
    val altEmail: String? = null,
    val category: String? = null,
    @SerialName("sub_category")
    val subCategory: String? = null,
    val location: String? = null,
    val remark: String? = null,
    val status: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("erp_payload")
    val erpPayload: SupportTicketErpPayload? = null,
    @SerialName("erp_response")
    val erpResponse: SupportTicketErpResponse? = null
)

@Serializable
data class SupportTicketErpPayload(
    val mobile: String? = null,
    @SerialName("customer_id")
    val customerId: Int? = null,
    val issue: String? = null,
    val priority: String? = null,
    val category: String? = null,
    @SerialName("sub_category")
    val subCategory: String? = null,
    val location: String? = null,
    val remark: String? = null,
    @SerialName("erp_ticket_id")
    val erpTicketId: String? = null
)

@Serializable
data class SupportTicketErpResponse(
    @SerialName("ticket_id")
    val ticketId: String? = null,
    val id: String? = null,
    val status: String? = null,
    val priority: String? = null,
    @SerialName("assigned_to")
    val assignedTo: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)
