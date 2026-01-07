package com.business.cmpproject.data.model.response

import kotlinx.serialization.Serializable


@Serializable
data class HomeResponse(
    val totalActivePlans: Int,
    val openTickets: Int,
    val pendingTickets: Int,
    val closedTickets: Int,
    val customerPlanSum: Double,
    val thisMonthInvoiceAmount: Double,
    val totalDueAmount: Double,
    val totalInvoices: Int,
    val recentInvoices: List<Invoice>,
    val recentPayments: List<Payments>,
    val recentTickets: List<Ticket>
)