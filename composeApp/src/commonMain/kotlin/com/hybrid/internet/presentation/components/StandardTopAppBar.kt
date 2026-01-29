package com.hybrid.internet.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.hybrid.internet.presentation.theme.CreamSurface
import com.hybrid.internet.presentation.theme.DarkSurface
import com.hybrid.internet.presentation.theme.DarkTextPrimary
import com.hybrid.internet.presentation.theme.GreenPrimary
import com.hybrid.internet.presentation.theme.GreenSecondary
import com.hybrid.internet.presentation.theme.LightTextPrimary
import com.hybrid.internet.presentation.theme.PinkPrimary
import com.hybrid.internet.presentation.theme.PinkSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardTopAppBar(
    title: String,
    subtitle: String? = null,
    isTitleCenter: Boolean = true,
    showBack: Boolean = false,
    onBack: () -> Unit = {},
    // New Menu Props
    menuItems: List<Pair<String, () -> Unit>> = emptyList(), // Pair of "Label" to "Action"
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val isDark = isSystemInDarkTheme()
    val accentColor = if (isDark) PinkPrimary else GreenPrimary
    var menuExpanded by remember { mutableStateOf(false) }

    val titleContent = @Composable {
        Column(horizontalAlignment = if (isTitleCenter) Alignment.CenterHorizontally else Alignment.Start) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = if (isDark) DarkTextPrimary else LightTextPrimary
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isDark) PinkSecondary else GreenSecondary
                )
            }
        }
    }

    val actionsContent = @Composable {
        if (menuItems.isNotEmpty()) {
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = accentColor
                    )
                }

                // --- PREMIUM DROPDOWN MENU ---
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier.background(if (isDark) DarkSurface else CreamSurface)
                ) {
                    menuItems.forEach { (label, onClick) ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = label,
                                    color = if (isDark) DarkTextPrimary else LightTextPrimary
                                )
                            },
                            onClick = {
                                menuExpanded = false
                                onClick()
                            }
                        )
                    }
                }
            }
        }
    }

    if (isTitleCenter) {
        CenterAlignedTopAppBar(
            title = titleContent,
            navigationIcon = { TopBarBackIcon(showBack, onBack, accentColor) },
            actions = { actionsContent() },
            scrollBehavior = scrollBehavior,
            colors = topAppBarColors(isDark)
        )
    } else {
        TopAppBar(
            title = titleContent,
            navigationIcon = { TopBarBackIcon(showBack, onBack, accentColor) },
            actions = { actionsContent() },
            scrollBehavior = scrollBehavior,
            colors = topAppBarColors(isDark)
        )
    }
}

@Composable
private fun TopBarBackIcon(show: Boolean, onBack: () -> Unit, tint: Color) {
    if (show) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = tint)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun topAppBarColors(isDark: Boolean) = TopAppBarDefaults.centerAlignedTopAppBarColors(
    containerColor = Color.Transparent,
    scrolledContainerColor = if (isDark) DarkSurface else CreamSurface
)

@Composable
private fun TopBarActionIcon(icon: ImageVector?, onClick: () -> Unit, tint: Color) {
    if (icon != null) {
        IconButton(onClick = onClick) {
            Icon(icon, contentDescription = "Action", tint = tint)
        }
    }
}