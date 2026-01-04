package com.business.cmpproject.presentation.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.business.cmpproject.core.log.AppLogger
import com.business.cmpproject.data.model.response.HomeResponse
import com.business.cmpproject.data.model.response.Invoice
import com.business.cmpproject.data.model.response.Ticket
import com.business.cmpproject.presentation.components.ScreenContainer

@Composable
fun HomeScreenContent(
    state: HomeResponse,
    onViewAllTickets: () -> Unit,
    onTicketClick: (Ticket) -> Unit,
    onViewAllInvoices: () -> Unit,
    onInvoiceClick: (Invoice) -> Unit,
//    onDashboardDataFetchSuccess: () -> Unit
) {

//    LaunchedEffect(Unit) {
//        onDashboardDataFetchSuccess()
//    }

    ScreenContainer {

        /* ---------- HEADER ---------- */
        Text(
            text = "Dashboard",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Here’s what’s happening with your account",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(Modifier.height(24.dp))

        /* ---------- RECENT TICKETS ---------- */
        SectionHeader(
            title = "Recent Tickets",
            onViewAll = onViewAllTickets
        )

        Spacer(Modifier.height(12.dp))

        if (state.recentTickets!!.isEmpty()) {
            AppLogger.d(
                "DashboardList",
                message = "Recent tickets are empty"
            )
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(
                    items = state.recentTickets,
                    key = { it.id!! }
                ) { ticket ->
                    TicketCard(
                        ticket = ticket,
                        onClick = { onTicketClick(ticket) }
                    )
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        /* ---------- ACCOUNT OVERVIEW ---------- */
        Text(
            text = "Account Overview",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(12.dp))

        DashboardStatsGrid(state)

        Spacer(Modifier.height(28.dp))

        /* ---------- RECENT INVOICES ---------- */
        SectionHeader(
            title = "Recent Invoices",
            onViewAll = onViewAllInvoices
        )

        Spacer(Modifier.height(12.dp))

        if (state.recentInvoices!!.isEmpty()) {
            AppLogger.d(
                "DashboardList",
                message = "Recent invoices are empty"
            )
        } else {
            state.recentInvoices.forEach { invoice ->
                InvoiceRow(
                    invoice = invoice,
                    onClick = { onInvoiceClick(invoice) }
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

