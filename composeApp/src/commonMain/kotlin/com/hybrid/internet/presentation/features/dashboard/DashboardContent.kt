package com.hybrid.internet.presentation.features.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.hybrid.internet.presentation.components.StandardTopAppBar
import com.hybrid.internet.presentation.features.statusTracking.PlanTrackingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent() {
    TabNavigator(HomeTab) { tabNavigator ->
        // 1. Get the current tab to determine UI values
        val currentTab = tabNavigator.current
        val navigator = LocalNavigator.currentOrThrow
        val rootNavigator = navigator.parent ?: navigator

        // 2. Dynamic Logic based on the active Tab
        val topBarTitle = currentTab.options.title

        // Custom logic for Subtitles or Menus per tab
        val topBarSubtitle = when (currentTab) {
            is PlansTab -> "Manage Subscriptions"
            is ServicesTab -> "Manage new link requests"
            is TicketsTab -> "Manage Tickets"
//            is InvoicesTab -> "Billing History"
            else -> null
        }

        val topBarMenuItems = when (currentTab) {
            is PlansTab -> listOf("Track Status" to { rootNavigator.push(PlanTrackingScreen()) })
//            is InvoicesTab -> listOf("Download All" to { /* action */ })
            else -> emptyList()
        }

        Scaffold(
            topBar = {
                when(currentTab){
                    is PlansTab, is TicketsTab, is ServicesTab -> {
                            // Now the TopBar is Dynamic!
                            StandardTopAppBar(
                                title = topBarTitle,
                                subtitle = topBarSubtitle,
                                showBack = false, // Dashboard usually has no back button
                                isTitleCenter = false,
                                menuItems = topBarMenuItems
                            )

                    }

                    else -> null
                }
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    TabNavigationItem(HomeTab)
                    TabNavigationItem(PlansTab)
                    TabNavigationItem(TicketsTab)
                    TabNavigationItem(ServicesTab)
                    TabNavigationItem(ProfileTab)
                }
            },
            // Use this to remove the "Extra Margin" at the top
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { paddingValues ->
            // IMPORTANT: Use paddingValues so content starts below the dynamic TopBar
            Box(modifier = Modifier.padding(paddingValues)) {
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