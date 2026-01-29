package com.hybrid.internet.presentation.features.statusTracking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hybrid.internet.data.model.response.PlanRequestItem
import com.hybrid.internet.presentation.features.home.StatusChip
import com.hybrid.internet.presentation.theme.CreamSurface
import com.hybrid.internet.presentation.theme.DarkSurface
import com.hybrid.internet.presentation.theme.DarkTextPrimary
import com.hybrid.internet.presentation.theme.DarkTextSecondary
import com.hybrid.internet.presentation.theme.GreenPrimary
import com.hybrid.internet.presentation.theme.LightTextPrimary
import com.hybrid.internet.presentation.theme.LightTextSecondary
import com.hybrid.internet.presentation.theme.PinkPrimary

@Composable
fun PlanTrackingContent(
    item: PlanRequestItem,
    isDark: Boolean
) {
    // Action styling logic
    val (actionLabel, actionColor, actionIcon) = when (item.actionType.lowercase()) {
        "upgrade" -> Triple("UPGRADE REQUEST", if (isDark) PinkPrimary else GreenPrimary, Icons.Default.KeyboardDoubleArrowUp)
        "downgrade" -> Triple("DOWNGRADE REQUEST", Color(0xFFEF6C00), Icons.Default.KeyboardDoubleArrowDown)
        "terminate" -> Triple("TERMINATION REQUEST", Color(0xFFD32F2F), Icons.Default.Block)
        else -> Triple(item.actionType.uppercase(), Color.Gray, Icons.Default.Info)
    }

    // Using your screen's background color instead of black
    val accentColor = if (isDark) PinkPrimary else GreenPrimary
    val surfaceColor = if (isDark) DarkSurface else CreamSurface
    val primaryText = if (isDark) DarkTextPrimary else LightTextPrimary
    val secondaryText = if (isDark) DarkTextSecondary else LightTextSecondary

    Card(
        modifier = Modifier
            .fillMaxWidth(),
//            .padding(horizontal = 14.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
//        border = BorderStroke(1.dp, if (isDark) Color.White.copy(0.1f) else Color(0xFFE0E6ED)) // Light blue-ish border
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // --- Header: Action Type & Status ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = null,
                        tint = actionColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = actionLabel,
                        style = MaterialTheme.typography.labelLarge,
                        color = actionColor,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                }

                StatusChip(item.status)
            }

            Spacer(Modifier.height(12.dp))

            // --- Full Title (No Clipping) ---
            Text(
                text = item.item,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = primaryText,
                lineHeight = 22.sp // Better readability for multi-line text
            )

            Spacer(Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(12.dp), tint = Color.Gray)
                Spacer(Modifier.width(4.dp))
                Text(
                    text = item.location,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }

            // --- Quantity Comparison Table ---
            // Simple, clean layout for quantity change
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .background(
                        color = if (isDark) Color(0xFF2C2C2C) else Color(0xFFF4F7FA), // Subtle Blue-Grey shade
                        shape = RoundedCornerShape(12.dp),

                    )
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("OLD QTY", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text(
                        "${item.oldQty}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = primaryText
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = actionColor.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("NEW QTY", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text(
                        "${item.newQty}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = actionColor
                    )
                }
            }

            // --- Reason Section ---
            if (item.reason.isNotBlank()) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "REASON",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Text(
                    text = item.reason,
                    style = MaterialTheme.typography.bodySmall,
                    color = primaryText.copy(alpha = 0.8f)
                )
            }

            // --- Admin Remark Banner ---
            if (!item.adminRemark.isNullOrBlank()) {
                Spacer(Modifier.height(12.dp))
                Surface(
                    color = if(isDark) Color(0xFF3D2626) else Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Feedback, null, tint = Color.Red, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Admin Note: ${item.adminRemark}",
                            style = MaterialTheme.typography.labelSmall,
                            color = if(isDark) Color(0xFFFF8A80) else Color(0xFFD32F2F),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}