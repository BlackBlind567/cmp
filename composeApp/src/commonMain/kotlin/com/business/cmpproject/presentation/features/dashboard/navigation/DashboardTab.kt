package com.business.cmpproject.presentation.features.dashboard.navigation

import com.business.cmpproject.composeapp.generated.resources.ic_home_new
import org.jetbrains.compose.resources.DrawableResource
import com.business.cmpproject.composeapp.generated.resources.Res
import com.business.cmpproject.composeapp.generated.resources.ic_orders_new
import com.business.cmpproject.composeapp.generated.resources.ic_search_new
import com.business.cmpproject.composeapp.generated.resources.ic_profile_new


enum class DashboardTab(
    val title: String,
    val icon: DrawableResource
) {
    HOME(
        title = "Home",
        icon = Res.drawable.ic_home_new
    ),

    SEARCH(
        title = "Search",
        icon = Res.drawable.ic_search_new
    ),

    ORDERS(
        title = "Orders",
        icon = Res.drawable.ic_orders_new
    ),

    PROFILE(
        title = "Profile",
        icon = Res.drawable.ic_profile_new
    );
}