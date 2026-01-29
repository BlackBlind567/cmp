package com.hybrid.internet.presentation.features.ticket

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.hybrid.internet.data.model.response.TicketData
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

// Compose Material (CMP standard)
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface

// Runtime & State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hybrid.internet.presentation.features.home.StatusChip
import com.hybrid.internet.presentation.theme.DarkSurface
import com.hybrid.internet.presentation.theme.GreenPrimary
import com.hybrid.internet.presentation.theme.PinkPrimary

@Composable
fun TicketHistoryContent(
    item: TicketData,
    isDark: Boolean,
    onTicketClick: (Int) -> Unit
) {
    // Dynamic Accent and Surface colors based on Theme
    val accentColor = if (isDark) PinkPrimary else GreenPrimary
    val surfaceColor = if (isDark) DarkSurface else Color.White
    val mainText = if (isDark) Color.White else Color(0xFF1A1A1A)
    val secondaryText = if (isDark) Color.LightGray else Color.Gray

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        border = BorderStroke(
            width = 1.dp,
            color = if (isDark) Color.White.copy(0.1f) else Color(0xFFE0E6ED)
        ),
        onClick = {
            item.id?.let { onTicketClick(it) }
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // --- Header: ID & Status ---
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ID: #${item.ticketId ?: "N/A"}",
                    style = TextStyle(
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        color = accentColor,
                        letterSpacing = 0.5.sp
                    )
                )

                // Dynamic Status Badge (Using your Global Logic)
                StatusChip(status = item.status ?: "Open")
            }

            Spacer(modifier = Modifier.height(10.dp))

            // --- Category Title ---
            Text(
                text = item.category ?: "General Support",
                style = TextStyle(
                    color = mainText,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    letterSpacing = (-0.3).sp
                )
            )

            // Sub-info (Location or Date placeholder)
//            if (!item..isNullOrBlank()) {
//                Text(
//                    text = item.location!!,
//                    style = TextStyle(color = secondaryText, fontSize = 12.sp)
//                )
//            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = if (isDark) Color.White.copy(0.05f) else Color(0xFFF0F2F5),
                thickness = 1.dp
            )

            // --- Footer: Company Details ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = accentColor.copy(alpha = 0.1f),
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.padding(4.dp),
                            tint = accentColor
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.companyName ?: "No Company",
                        style = TextStyle(
                            color = mainText.copy(alpha = 0.7f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                // Small "View Details" arrow for premium touch
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = secondaryText
                )
            }
        }
    }
}