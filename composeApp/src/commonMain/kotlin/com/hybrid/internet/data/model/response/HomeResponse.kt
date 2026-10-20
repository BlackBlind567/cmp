package com.hybrid.internet.data.model.response

import kotlinx.serialization.SerialName
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
    @SerialName("dashboard_message")
    val dashboardMessage: String? = null,
    @SerialName("dashboard_button")
    val dashboardButton: String? = null,
    @SerialName("dashboard_link")
    val dashboardLink: String? = null,
    val totalInvoices: Int,
    val recentInvoices: List<Invoice>,
    val recentPayments: List<Payments>,
    val recentTickets: List<Ticket>
)
