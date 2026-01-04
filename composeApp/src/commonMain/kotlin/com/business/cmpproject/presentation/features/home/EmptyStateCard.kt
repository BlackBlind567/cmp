package com.business.cmpproject.presentation.features.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.business.cmpproject.presentation.theme.DarkTextSecondary
import com.business.cmpproject.presentation.theme.LightTextSecondary

@Composable
fun EmptyStateCard(message: String) {
    val isDark = isSystemInDarkTheme()

    OutlinedCard(
        modifier = Modifier.fillMaxWidth().height(100.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.2f)),
        colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.Inbox,
                    contentDescription = null,
                    tint = Color.Gray.copy(alpha = 0.5f),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isDark) DarkTextSecondary else LightTextSecondary
                )
            }
        }
    }
}