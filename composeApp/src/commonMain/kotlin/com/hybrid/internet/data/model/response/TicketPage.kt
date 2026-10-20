package com.hybrid.internet.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive

@Serializable
class TicketPage(
    val customer: TicketCustomer? = null,
    val pagination: TicketPagination? = null,

    private val tickets: List<TicketData>? = null,

    @SerialName("current_page")
    private val legacyCurrentPage: Int? = null,

    @SerialName("data")
    private val legacyData: List<TicketData>? = null,

    @SerialName("first_page_url")
    private val legacyFirstPageUrl: String? = null,

    @SerialName("from")
    private val legacyFrom: Int? = null,

    @SerialName("last_page")
    private val legacyLastPage: Int? = null,

    @SerialName("last_page_url")
    private val legacyLastPageUrl: String? = null,

    @SerialName("next_page_url")
    private val legacyNextPageUrl: String? = null,

    @SerialName("prev_page_url")
    private val legacyPrevPageUrl: String? = null,

    @SerialName("path")
    private val legacyPath: String? = null,

    @SerialName("per_page")
    private val legacyPerPage: Int? = null,

    @SerialName("to")
    private val legacyTo: Int? = null,

    @SerialName("total")
    private val legacyTotal: Int? = null
) {
    val currentPage: Int?
        get() = pagination?.page ?: legacyCurrentPage

    val data: List<TicketData>
        get() = tickets ?: legacyData.orEmpty()

    val firstPageUrl: String?
        get() = legacyFirstPageUrl

    val from: Int?
        get() = legacyFrom

    val lastPage: Int?
        get() = pagination?.totalPages ?: legacyLastPage ?: 1

    val lastPageUrl: String?
        get() = legacyLastPageUrl

    val nextPageUrl: String?
        get() = legacyNextPageUrl

    val prevPageUrl: String?
        get() = legacyPrevPageUrl

    val path: String?
        get() = legacyPath

    val perPage: Int?
        get() = pagination?.limit ?: legacyPerPage

    val to: Int?
        get() = legacyTo

    val total: Int?
        get() = pagination?.total ?: legacyTotal
}

@Serializable
data class TicketCustomer(
    val mobile: String? = null,
    val name: String? = null,
    @SerialName("customer_id")
    val customerId: String? = null
)

@Serializable
data class TicketPagination(
    val page: Int? = null,
    val limit: Int? = null,
    val total: Int? = null,
    val totalPages: Int? = null,
    val hasNextPage: Boolean? = null,
    val hasPrevPage: Boolean? = null
)

@Serializable
data class TicketData(
    @SerialName("id")
    private val rawId: JsonElement? = null,

    @SerialName("ticket_id")
    val ticketId: String? = null,

    @SerialName("customer_id")
    private val rawCustomerId: JsonElement? = null,

    @SerialName("company_name")
    private val legacyCompanyName: String? = null,

    @SerialName("customer_name")
    private val legacyCustomerName: String? = null,

    @SerialName("alt_mobile")
    val altMobile: String? = null,

    @SerialName("alt_email")
    val altEmail: String? = null,

    @SerialName("category")
    private val legacyCategory: String? = null,

    @SerialName("remark")
    private val legacyRemark: String? = null,
    val status: String? = null,
    val priority: String? = null,
    val issue: String? = null,

    @SerialName("assigned_to")
    val assignedTo: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null
) {
    val id: String?
        get() = rawId?.jsonPrimitive?.contentOrNull

    val numericId: Int?
        get() = rawId?.jsonPrimitive?.intOrNull

    val customerId: Int?
        get() = rawCustomerId?.jsonPrimitive?.intOrNull

    val companyName: String?
        get() = assignedTo ?: legacyCompanyName ?: legacyCustomerName

    val customerName: String?
        get() = legacyCustomerName

    val category: String?
        get() = legacyCategory ?: issue

    val remark: String?
        get() = legacyRemark ?: issue
}
