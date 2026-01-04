package com.business.cmpproject.presentation.features.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.business.cmpproject.data.model.response.Ticket

@Composable
fun TicketCard(
    ticket: Ticket,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.width(240.dp),
        shape = MaterialTheme.shapes.large,
        tonalElevation = 2.dp,
        onClick = onClick
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(
                text = ticket.category!!,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Ticket ID: ${ticket.ticketId}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(Modifier.height(12.dp))

            StatusChip(ticket.status!!)

            Spacer(Modifier.height(8.dp))

            Text(
                text = ticket.createdAt!!,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
            )
        }
    }
}
