package com.business.cmpproject.presentation.features.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Receipt
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
import com.business.cmpproject.data.model.response.Invoice
import com.business.cmpproject.presentation.theme.CreamSurface
import com.business.cmpproject.presentation.theme.DarkSurface
import com.business.cmpproject.presentation.theme.DarkTextPrimary
import com.business.cmpproject.presentation.theme.DarkTextSecondary
import com.business.cmpproject.presentation.theme.GreenPrimary
import com.business.cmpproject.presentation.theme.GreenSecondary
import com.business.cmpproject.presentation.theme.LightTextPrimary
import com.business.cmpproject.presentation.theme.LightTextSecondary
import com.business.cmpproject.presentation.theme.PinkPrimary

@Composable
fun PremiumInvoiceRow(
    invoice: Invoice,
    isDark: Boolean,
    onClick: () -> Unit
) {
    val accentColor = if (isDark) PinkPrimary else GreenPrimary
    val surfaceColor = if (isDark) DarkSurface else CreamSurface

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Placeholder
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = accentColor.copy(alpha = 0.1f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Receipt, contentDescription = null, tint = accentColor)
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = invoice.invoiceNo ?: "N/A",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) DarkTextPrimary else LightTextPrimary
                )
                Text(
                    text = invoice.invoiceDate ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isDark) DarkTextSecondary else LightTextSecondary
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "₹${invoice.netAmount}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = if (isDark) DarkTextPrimary else LightTextPrimary
                )
                Text(
                    text = invoice.paymentStatus ?: "Unpaid",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (invoice.paymentStatus?.lowercase() == "paid") GreenSecondary else Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}