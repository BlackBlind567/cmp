package com.hybrid.internet.presentation.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hybrid.internet.data.model.response.HomeResponse
import com.hybrid.internet.data.model.response.Invoice
import com.hybrid.internet.data.model.response.Ticket
import com.hybrid.internet.presentation.theme.CreamSurface
import com.hybrid.internet.presentation.theme.DarkSurface
import com.hybrid.internet.presentation.theme.GreenPrimary
import com.hybrid.internet.presentation.theme.PinkPrimary

@Composable
fun HomeScreenContent(data: HomeResponse, isDark: Boolean,
                      onViewAllTickets: () -> Unit,
                      onTicketClick: (Ticket) -> Unit,
                      onViewAllInvoices: () -> Unit,
                      onInvoiceClick: (Invoice) -> Unit) {
    val accentColor = if (isDark) PinkPrimary else GreenPrimary
    val surfaceColor = if (isDark) DarkSurface else CreamSurface
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        // --- 1. Top Welcome Bar ---
        item {
            DashboardTopBar(name = "Aakash", company = "Atoms Group", isDark = isDark)
        }

        // --- 2. Main Financial Highlight ---
        item {
            TotalDueCard(data.totalDueAmount, data.thisMonthInvoiceAmount, surfaceColor, accentColor)
        }

        // --- 3. Quick Stats Grid ---
        item {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("Active Plans", "${data.totalActivePlans}", Icons.Default.Inventory,  Modifier.weight(1f), isDark)
                StatCard("Total Invoices", "${data.totalInvoices}", Icons.Default.Description, Modifier.weight(1f), isDark)
            }
        }

        // --- 4. Ticket Status Row (Glassmorphism style) ---
        item {
            SectionHeader("Ticket Overview","View All", onViewAllTickets)
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TicketMiniChip("Open", data.openTickets, Color(0xFFE53935), Modifier.weight(1f))
                TicketMiniChip("Pending", data.pendingTickets, Color(0xFFF57C00), Modifier.weight(1f))
                TicketMiniChip("Closed", data.closedTickets, Color(0xFF4CAF50), Modifier.weight(1f))
            }
        }

        // --- 5. Recent Invoices List ---
        item { SectionHeader("Recent Invoices", "View All") }
        items(data.recentInvoices.take(3)) { invoice ->
            DashboardInvoiceItem(invoice, isDark)
        }

        // --- 6. Recent Tickets ---
        item { SectionHeader("Active Support", null) }
        items(data.recentTickets.take(2)) { ticket ->
            DashboardTicketItem(ticket, isDark)
        }
        item { SectionHeader("Recent Payments", "View All") }

        if (data.recentPayments.isEmpty()) {
            item {
                Text(
                    "No recent payments found",
                    modifier = Modifier.padding(20.dp),
                    color = Color.Gray
                )
            }
        } else {
            items(data.recentPayments.take(3)) { payment ->
                DashboardPaymentItem(payment, isDark)
            }
        }

    }
}