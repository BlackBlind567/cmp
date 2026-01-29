package com.hybrid.internet.presentation.features.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybrid.internet.data.model.response.Ticket
import com.hybrid.internet.presentation.theme.CreamSurface
import com.hybrid.internet.presentation.theme.DarkSurface
import com.hybrid.internet.presentation.theme.DarkTextPrimary
import com.hybrid.internet.presentation.theme.DarkTextSecondary
import com.hybrid.internet.presentation.theme.GreenPrimary
import com.hybrid.internet.presentation.theme.LightTextPrimary
import com.hybrid.internet.presentation.theme.LightTextSecondary
import com.hybrid.internet.presentation.theme.PinkPrimary

@Composable
fun PremiumTicketCard(
    ticket: Ticket,
    isDark: Boolean,
    onClick: () -> Unit
) {
    val accentColor = if (isDark) PinkPrimary else GreenPrimary
    val surfaceColor = if (isDark) DarkSurface else CreamSurface

    Card(
        modifier = Modifier.width(260.dp), // Fixed width for carousel
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Category Chip
                StatusChip(ticket.status ?: "Open")
                Spacer(Modifier.weight(1f))
                Text(
                    text = ticket.category ?: "Support",
                    style = MaterialTheme.typography.labelSmall,
                    color = accentColor
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Ticket #${ticket.ticketId}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isDark) DarkTextPrimary else LightTextPrimary
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Created: ${ticket.createdAt}",
                style = MaterialTheme.typography.bodySmall,
                color = if (isDark) DarkTextSecondary else LightTextSecondary
            )

            Spacer(Modifier.height(12.dp))

            HorizontalDivider(thickness = 0.5.dp, color = accentColor.copy(alpha = 0.1f))

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Tap to view details →",
                style = MaterialTheme.typography.labelSmall,
                color = accentColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}