package com.hybrid.internet.presentation.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybrid.internet.data.model.response.Ticket

@Composable
fun DashboardTicketItem(ticket: Ticket, isDark: Boolean) {
    val mainText = if (isDark) Color.White else Color(0xFF1A1A1A)
    val cardBg = if (isDark) Color(0xFF252525) else Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .background(cardBg, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon with soft background
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(Color.Blue.copy(0.05f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.SupportAgent,
                contentDescription = null,
                tint = Color.Blue.copy(0.6f),
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "ID: ${ticket.ticketId}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = ticket.category ?: "Support Request",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.ExtraBold,
                color = mainText
            )
            Text(
                text = ticket.createdAt ?: "",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }

        StatusChip(ticket.status ?: "Open")
    }
}