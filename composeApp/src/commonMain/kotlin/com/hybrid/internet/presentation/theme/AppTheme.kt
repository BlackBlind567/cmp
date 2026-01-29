package com.hybrid.internet.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()

    MaterialTheme(
        colorScheme = if (isDarkTheme) DarkAppColorScheme else LightAppColorScheme,
        content = content
    )
}