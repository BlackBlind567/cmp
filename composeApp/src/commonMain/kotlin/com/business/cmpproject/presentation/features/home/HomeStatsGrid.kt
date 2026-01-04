package com.business.cmpproject.presentation.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.business.cmpproject.data.model.response.HomeResponse

@Composable
fun DashboardStatsGrid(state: HomeResponse) {
    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Active Plans", state.totalActivePlans.toString(),  modifier = Modifier.weight(1f))
            StatCard("Open Tickets", state.openTickets.toString(),  modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Total Due", "₹${state.totalDueAmount}",  modifier = Modifier.weight(1f))
            StatCard("This Month", "₹${state.thisMonthInvoiceAmount}",  modifier = Modifier.weight(1f))
        }
    }
}
