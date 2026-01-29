package com.hybrid.internet.presentation.features.ticketHistory

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hybrid.internet.data.model.response.TicketHistoryItem
import com.hybrid.internet.presentation.features.home.StatusChip
import com.hybrid.internet.presentation.theme.GreenPrimary
import com.hybrid.internet.presentation.theme.PinkPrimary

@Composable
fun TicketHistoryTimelineItem(
    item: TicketHistoryItem,
    isLast: Boolean,
    isDark: Boolean
) {
    val accentColor = if (isDark) PinkPrimary else GreenPrimary
    val mainText = if (isDark) Color.White else Color(0xFF1A1A1A)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // --- Timeline Vertical Line & Dot ---
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(accentColor, CircleShape)
                    .border(3.dp, accentColor.copy(0.2f), CircleShape)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(80.dp) // Adjust based on content
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(accentColor, Color.Transparent)
                            )
                        )
                )
            }
        }

        Spacer(Modifier.width(16.dp))

        // --- Content Card ---
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) Color(0xFF252525) else Color.White
            ),
            elevation = CardDefaults.cardElevation(0.dp),
            border = BorderStroke(1.dp, if (isDark) Color.White.copy(0.05f) else Color(0xFFE0E6ED))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "Ticket #${item.ticketId}",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                        color = accentColor
                    )
                    StatusChip(item.status!!)
                }

                Spacer(Modifier.height(8.dp))

                // Full Message (No clipping)
                Text(
                    text = item.message!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = mainText,
                    lineHeight = 20.sp
                )

                Spacer(Modifier.height(12.dp))

                // Footer: Company & Customer
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Business, null, modifier = Modifier.size(12.dp), tint = Color.Gray)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${item.customerName} @ ${item.companyName}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}