package com.business.cmpproject.presentation.features.dashboard

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator

@Composable
fun DashboardContent() {
    // Setting HomeTab here ensures it is the default first page
    TabNavigator(HomeTab) {
        Scaffold(
            bottomBar = {
                // Using Material 3 NavigationBar for a premium look
                NavigationBar(
                    containerColor = Color.White, // Or a very light surface color
                    tonalElevation = 10.dp,
                    windowInsets = WindowInsets(bottom = 10.dp) // Adds padding from screen edge
                ) {
                    TabNavigationItem(HomeTab)
                    TabNavigationItem(InvoicesTab)
                    TabNavigationItem(TicketsTab)
                    TabNavigationItem(PlansTab)
                    TabNavigationItem(ProfileTab)
                }
            }
        ) { paddingValues ->
            // Apply padding so content isn't hidden behind the bottom bar
            Surface(modifier = Modifier.padding(paddingValues)) {
                CurrentTab()
            }
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    val isSelected = tabNavigator.current == tab

    NavigationBarItem(
        selected = isSelected,
        onClick = { tabNavigator.current = tab },
        label = {
            Text(
                text = tab.options.title,
                style = MaterialTheme.typography.labelSmall
            )
        },
        icon = {
            Icon(
                painter = tab.options.icon!!,
                contentDescription = tab.options.title,
                // M3 handles color automatically, but you can tweak it:
                tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
            )
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer // This adds the premium oval background
        )
    )
}