package com.hybrid.internet.presentation.features.plans

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hybrid.internet.data.model.response.PlanResponse
import com.hybrid.internet.presentation.theme.CreamSurface
import com.hybrid.internet.presentation.theme.DarkSurface
import com.hybrid.internet.presentation.theme.DarkTextPrimary
import com.hybrid.internet.presentation.theme.DarkTextSecondary
import com.hybrid.internet.presentation.theme.GreenPrimary
import com.hybrid.internet.presentation.theme.LightTextPrimary
import com.hybrid.internet.presentation.theme.LightTextSecondary
import com.hybrid.internet.presentation.theme.PinkPrimary

@Composable
fun CustomerPlanCard(
    plan: PlanResponse,
    onManageClick: (PlanResponse) -> Unit,
    onCardClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    // Theme Colors
    val accentColor = if (isDark) PinkPrimary else GreenPrimary
    val surfaceColor = if (isDark) DarkSurface else CreamSurface
    val primaryText = if (isDark) DarkTextPrimary else LightTextPrimary
    val secondaryText = if (isDark) DarkTextSecondary else LightTextSecondary

    Card(
        modifier = Modifier.fillMaxWidth().clickable{
            onCardClick()
        },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header: Title & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = plan.item ?: "Internet Plan",
                        style = MaterialTheme.typography.titleMedium,
                        color = primaryText,
                        fontWeight = FontWeight.Bold,
                        maxLines = 4,
//                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Circuit ID: ${plan.circuitId ?: "N/A"}",
                        style = MaterialTheme.typography.labelMedium,
                        color = accentColor
                    )
                }

                StatusBadge(isActive = plan.status == 1, accentColor = accentColor)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Info Grid
            Row(modifier = Modifier.fillMaxWidth()) {
                DetailItem(Modifier.weight(1f), "Location", plan.locationName ?: "N/A", secondaryText, primaryText)
                DetailItem(Modifier.weight(1f), "Billing", plan.billing_cycle ?: "Monthly", secondaryText, primaryText)
                DetailItem(Modifier.weight(0.8f), "Qty", "${plan.qty} ${plan.itemUnit}", secondaryText, primaryText)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Price Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Monthly Total", style = MaterialTheme.typography.labelSmall, color = secondaryText)
                    Text(
                        text = "₹${plan.amount}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = primaryText,
                        fontWeight = FontWeight.Black
                    )
                }

                // Premium Manage Button
                Button(
                    onClick = { onManageClick(plan) },
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Manage", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
fun StatusBadge(isActive: Boolean, accentColor: Color) {
    Surface(
        color = accentColor.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.2f))
    ) {
        Text(
            text = if (isActive) "ACTIVE" else "PENDING",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = accentColor,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
fun DetailItem(modifier: Modifier, label: String, value: String, labelColor: Color, valueColor: Color) {
    Column(modifier = modifier) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = labelColor)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = valueColor,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}