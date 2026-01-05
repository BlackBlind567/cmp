package com.business.cmpproject.presentation.features.dashboard

import com.business.cmpproject.presentation.features.dashboard.navigation.DashboardTab


sealed class DashboardEvent {
    data class TabSelected(val tab: DashboardTab) : DashboardEvent()
}
