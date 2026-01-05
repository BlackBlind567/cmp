package com.business.cmpproject.presentation.features.dashboard.navigation

import androidx.compose.runtime.Composable
import com.business.cmpproject.presentation.features.dashboard.tabs.home.HomeTabScreen

import com.business.cmpproject.presentation.features.dashboard.tabs.orders.OrdersTabScreen
import com.business.cmpproject.presentation.features.dashboard.tabs.profile.ProfileTabScreen
import com.business.cmpproject.presentation.features.dashboard.tabs.search.SearchTabScreen

@Composable
fun DashboardNavigator(tab: DashboardTab) {
    when (tab) {
        DashboardTab.HOME -> HomeTabScreen()
        DashboardTab.SEARCH -> SearchTabScreen()
        DashboardTab.ORDERS -> OrdersTabScreen()
        DashboardTab.PROFILE -> ProfileTabScreen()
    }
}