package com.hybrid.internet.presentation.features.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StatusChip(status: String) {
    val isDark = isSystemInDarkTheme()

    // Define Color Pairs (Background to Text/Border)
    val (bg, textColor) = when (status.lowercase()) {
        // --- Ticket/Invoice Statuses ---
        "open" -> Color(0xFFE3F2FD) to Color(0xFF1565C0)
        "unpaid" -> Color(0xFFFFEBEE) to Color(0xFFC62828)
        "paid" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)

        // --- Plan Tracking Statuses (New) ---


        "pending"-> if (isDark) Color(0xFF332D00) to Color(0xFFFFD600) else Color(0xFFFFF9C4) to Color(0xFFF57F17)
        "pendilng" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
        "rejected" -> Color(0xFFFFEBEE) to Color(0xFFC62828)

        // --- Default ---
        else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        color = bg,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, textColor.copy(alpha = 0.2f)) // Added subtle border for premium look
    ) {
        Text(
            text = status.uppercase(), // Uppercase looks more like a formal badge
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Black // Bold text for better readability
        )
    }
}