package com.hybrid.internet.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketPage(
    @SerialName("current_page")
    val currentPage: Int,

    val data: List<TicketData>,

    @SerialName("first_page_url")
    val firstPageUrl: String? = null,

    val from: Int? = null,

    @SerialName("last_page")
    val lastPage: Int? = null,

    @SerialName("last_page_url")
    val lastPageUrl: String? = null,

    @SerialName("next_page_url")
    val nextPageUrl: String? = null,

    @SerialName("prev_page_url")
    val prevPageUrl: String? = null,

    val path: String? = null,

    @SerialName("per_page")
    val perPage: Int? = null,

    val to: Int? = null,
    val total: Int? = null
)


@Serializable
data class TicketData(
    val id: Int? = null,

    @SerialName("ticket_id")
    val ticketId: String? = null,

    @SerialName("customer_id")
    val customerId: Int? = null,

    @SerialName("company_name")
    val companyName: String? = null,

    @SerialName("customer_name")
    val customerName: String? = null,

    @SerialName("alt_mobile")
    val altMobile: String? = null,

    @SerialName("alt_email")
    val altEmail: String? = null,

    val category: String? = null,
    val remark: String? = null,
    val status: String? = null
)

