package com.hybrid.internet.presentation.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Lan
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybrid.internet.data.model.response.HomeResponse

@Composable
fun DashboardStatsGrid(state: HomeResponse, isDark: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // High Highlight Card for Due Amount
        StatCard(
            label = "Total Amount Due",
            value = "₹${state.totalDueAmount}",
            icon = Icons.Default.AccountBalanceWallet,
            isPrimary = true,
            isDark = isDark
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SmallStatCard("Active Plans", state.totalActivePlans.toString(), Icons.Default.Lan, Modifier.weight(1f), isDark)
            SmallStatCard("Open Tickets", state.openTickets.toString(), Icons.Default.ConfirmationNumber, Modifier.weight(1f), isDark)
        }
    }
}