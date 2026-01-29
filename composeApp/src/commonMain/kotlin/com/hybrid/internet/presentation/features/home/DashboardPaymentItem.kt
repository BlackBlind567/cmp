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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pending
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
import com.hybrid.internet.data.model.response.Payments
import com.hybrid.internet.presentation.theme.GreenPrimary

@Composable
fun DashboardPaymentItem(
    payment: Payments,
    isDark: Boolean
) {
    val mainText = if (isDark) Color.White else Color(0xFF1A1A1A)
    val cardBg = if (isDark) Color(0xFF252525) else Color.White

    // Status Logic: 1 usually means Success/Verified, 0 means Pending/Processing
    val statusColor = if (payment.status == 1) {
        if (isDark) GreenPrimary else Color(0xFF2E7D32)
    } else {
        Color(0xFFF57C00) // Orange for pending verification
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .background(cardBg, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- Icon Section ---
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(statusColor.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (payment.status == 1) Icons.Default.CheckCircle else Icons.Default.Pending,
                contentDescription = null,
                tint = statusColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(Modifier.width(14.dp))

        // --- Details Section ---
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Ref: ${payment.ref_no.ifBlank { "N/A" }}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (payment.status == 1) "Payment Verified" else "Verification Pending",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.ExtraBold,
                color = mainText
            )
            Text(
                text = payment.payment_date,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }

        // --- Amount & Mode ---
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "₹${payment.amount}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = mainText
            )
            Surface(
                color = if (isDark) Color.DarkGray else Color(0xFFF1F3F4),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = payment.mode.ifBlank { "Online" },
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isDark) Color.LightGray else Color.DarkGray
                )
            }
        }
    }
}