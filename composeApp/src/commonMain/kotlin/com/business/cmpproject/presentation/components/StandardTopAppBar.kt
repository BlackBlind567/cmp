package com.business.cmpproject.presentation.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.business.cmpproject.presentation.theme.CreamSurface
import com.business.cmpproject.presentation.theme.DarkSurface
import com.business.cmpproject.presentation.theme.DarkTextPrimary
import com.business.cmpproject.presentation.theme.GreenPrimary
import com.business.cmpproject.presentation.theme.GreenSecondary
import com.business.cmpproject.presentation.theme.LightTextPrimary
import com.business.cmpproject.presentation.theme.PinkPrimary
import com.business.cmpproject.presentation.theme.PinkSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardTopAppBar(
    title: String,
    subtitle: String? = null,
    showBack: Boolean = false, // Default is false as requested
    onBack: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val isDark = isSystemInDarkTheme()
    val accentColor = if (isDark) PinkPrimary else GreenPrimary

    CenterAlignedTopAppBar(
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = if (isDark) DarkTextPrimary else LightTextPrimary
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isDark) PinkSecondary else GreenSecondary
                    )
                }
            }
        },
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = accentColor
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent, // Let the Scaffold background show through
            scrolledContainerColor = if (isDark) DarkSurface else CreamSurface
        )
    )
}