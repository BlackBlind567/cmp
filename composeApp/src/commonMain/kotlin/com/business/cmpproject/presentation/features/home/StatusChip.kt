package com.business.cmpproject.presentation.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StatusChip(status: String) {

    val (bg, textColor) = when (status.lowercase()) {
        "open" -> Color(0xFFE3F2FD) to Color(0xFF1565C0)
        "unpaid" -> Color(0xFFFFEBEE) to Color(0xFFC62828)
        "paid" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
        else -> MaterialTheme.colorScheme.surfaceVariant to
                MaterialTheme.colorScheme.onSurfaceVariant
    }

    Text(
        text = status,
        color = textColor,
        modifier = Modifier
            .background(bg, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        style = MaterialTheme.typography.labelSmall
    )
}