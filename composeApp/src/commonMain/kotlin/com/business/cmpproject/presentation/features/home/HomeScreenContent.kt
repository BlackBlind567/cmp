package com.business.cmpproject.presentation.features.home

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.business.cmpproject.core.log.AppLogger
import com.business.cmpproject.data.model.response.HomeResponse
import com.business.cmpproject.data.model.response.Invoice
import com.business.cmpproject.data.model.response.Ticket
import com.business.cmpproject.presentation.components.ScreenContainer
import com.business.cmpproject.presentation.theme.CreamBackground
import com.business.cmpproject.presentation.theme.DarkBackground

@Composable
fun HomeScreenContent(
    state: HomeResponse,
    customerName: String,
    companyName: String,
    onViewAllTickets: () -> Unit,
    onTicketClick: (Ticket) -> Unit,
    onViewAllInvoices: () -> Unit,
    onInvoiceClick: (Invoice) -> Unit
) {
    val isDark = isSystemInDarkTheme()

    Scaffold(
        topBar = {
            DashboardTopBar(customerName, companyName, isDark)
        },
        containerColor = if (isDark) DarkBackground else CreamBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            /* ---------- ACCOUNT OVERVIEW (Cards) ---------- */
            Text(
                text = "Account Overview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))
            DashboardStatsGrid(state, isDark)

            Spacer(Modifier.height(28.dp))

            /* ---------- RECENT TICKETS (Carousel) ---------- */
            SectionHeader(
                title = "Recent Support Tickets",
                onViewAll = onViewAllTickets
            )
            Spacer(Modifier.height(12.dp))
            if (state.recentTickets.isNullOrEmpty()) {
                EmptyStateCard("No active tickets")
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(end = 16.dp)
                ) {
                    items(state.recentTickets) { ticket ->
                        PremiumTicketCard(ticket, isDark) { onTicketClick(ticket) }
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            /* ---------- RECENT INVOICES (Rows) ---------- */
            SectionHeader(
                title = "Recent Invoices",
                onViewAll = onViewAllInvoices
            )
            Spacer(Modifier.height(12.dp))
            if (state.recentInvoices.isNullOrEmpty()) {
                EmptyStateCard("No pending invoices")
            } else {
                state.recentInvoices.forEach { invoice ->
                    PremiumInvoiceRow(invoice, isDark) { onInvoiceClick(invoice) }
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

