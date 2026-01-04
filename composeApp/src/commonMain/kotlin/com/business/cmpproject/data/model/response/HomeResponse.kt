package com.business.cmpproject.data.model.response

import kotlinx.serialization.Serializable


@Serializable
data class HomeResponse(
    val totalActivePlans: Int? = null,
    val openTickets: Int? = null,
    val pendingTickets: Int? = null,
    val closedTickets: Int? = null,
    val customerPlanSum: Float? = null,
    val thisMonthInvoiceAmount: Float? = null,
    val totalDueAmount: Float? = null,
    val totalInvoices: Float? = null,
    val planDistribution: Map<String, Int>? = emptyMap(),
    val recentInvoices: List<Invoice>? = emptyList(),
    val recentTickets: List<Ticket>? = emptyList(),
//    val days: List<String>? = emptyList(),
//    val trendValues: List<Int>? = emptyList()
)