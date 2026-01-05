package com.business.cmpproject.presentation.features.dashboard

import com.business.cmpproject.presentation.features.dashboard.navigation.DashboardTab


data class DashboardState(
    val selectedTab: DashboardTab = DashboardTab.HOME
)