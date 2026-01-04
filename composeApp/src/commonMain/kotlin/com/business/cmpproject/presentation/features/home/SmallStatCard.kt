package com.business.cmpproject.presentation.features.home

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.business.cmpproject.presentation.theme.CreamSurface
import com.business.cmpproject.presentation.theme.DarkSurface
import com.business.cmpproject.presentation.theme.DarkTextPrimary
import com.business.cmpproject.presentation.theme.DarkTextSecondary
import com.business.cmpproject.presentation.theme.GreenPrimary
import com.business.cmpproject.presentation.theme.LightTextPrimary
import com.business.cmpproject.presentation.theme.LightTextSecondary
import com.business.cmpproject.presentation.theme.PinkPrimary

@Composable
fun SmallStatCard(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    isDark: Boolean = isSystemInDarkTheme()
) {
    val accentColor = if (isDark) PinkPrimary else GreenPrimary

    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (isDark) DarkSurface else CreamSurface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(24.dp)
            )

            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) DarkTextPrimary else LightTextPrimary
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isDark) DarkTextSecondary else LightTextSecondary
                )
            }
        }
    }
}