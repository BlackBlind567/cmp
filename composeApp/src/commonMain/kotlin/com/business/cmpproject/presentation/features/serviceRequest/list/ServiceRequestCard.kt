package com.business.cmpproject.presentation.features.serviceRequest.list

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.business.cmpproject.data.model.response.ServiceRequestItem
import com.business.cmpproject.presentation.features.home.StatusChip
import com.business.cmpproject.presentation.theme.DarkSurface
import com.business.cmpproject.presentation.theme.GreenPrimary
import com.business.cmpproject.presentation.theme.PinkPrimary

@Composable
fun ServiceRequestCard(item: ServiceRequestItem, isDark: Boolean) {
    val accentColor = if (isDark) PinkPrimary else GreenPrimary
    val surfaceColor = if (isDark) DarkSurface else Color.White
    val textColor = if (isDark) Color.White else Color(0xFF1A1A1A)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, if (isDark) Color.White.copy(0.1f) else Color(0xFFE0E6ED))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header: Type & Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusChip(status = item.reportType!!.replace("_", " "))
                Text(
                    text = item.createdAt!!.split(" ")[0], // Only Date
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }

            Spacer(Modifier.height(12.dp))

            // Location & Bandwidth
            Text(
                text = item.locationName!!,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = textColor
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Speed, null, modifier = Modifier.size(14.dp), tint = accentColor)
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "${item.bandwidth} Mbps Capacity",
                    style = MaterialTheme.typography.bodySmall,
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 0.5.dp,
                color = if (isDark) Color.White.copy(0.05f) else Color(0xFFF0F2F5)
            )

            // Infrastructure Details
            Row(modifier = Modifier.fillMaxWidth()) {
                InfoBlock("BTS Code", item.btsCode?:"", Modifier.weight(1f))
                InfoBlock("MUX ID", item.muxId?:"", Modifier.weight(1f))
            }

            if (!item.address!!.isBlank()) {
                Spacer(Modifier.height(12.dp))
                Row {
                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "item.address",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun InfoBlock(label: String, value: String, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.ExtraBold)
    }
}