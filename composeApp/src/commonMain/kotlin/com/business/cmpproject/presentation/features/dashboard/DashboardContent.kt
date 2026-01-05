package com.business.cmpproject.presentation.features.dashboard

import androidx.compose.runtime.*
import androidx.compose.material3.*

import com.business.cmpproject.presentation.features.dashboard.navigation.DashboardNavigator
import com.business.cmpproject.presentation.features.dashboard.navigation.DashboardTab
import org.jetbrains.compose.resources.painterResource

@Composable
fun DashboardContent(
    screenModel: DashboardScreenModel
) {
    val state by screenModel.state.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                DashboardTab.values().forEach { tab ->
                    NavigationBarItem(
                        selected = state.selectedTab == tab,
                        onClick = {
                            screenModel.onEvent(
                                DashboardEvent.TabSelected(tab)
                            )
                        },
                        icon = {
                            Icon(
                                painter = painterResource(tab.icon),
                                contentDescription = tab.title
                            )
                        },
                        label = { Text(tab.title) }
                    )
                }
            }
        }
    ) {
        DashboardNavigator(tab = state.selectedTab)
    }
}
