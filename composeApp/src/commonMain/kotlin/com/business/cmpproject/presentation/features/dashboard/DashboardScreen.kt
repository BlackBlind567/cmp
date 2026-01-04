package com.business.cmpproject.presentation.features.dashboard

import androidx.compose.material3.*
import androidx.compose.runtime.*
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator

class DashboardScreen : Screen {

    @Composable
    override fun Content() {
//        val screenModel = getScreenModel<DashboardScreenModel>()
//        val navigator = LocalNavigator.current!!

        DashboardContent(
        )
    }
}
