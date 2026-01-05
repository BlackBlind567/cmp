package com.business.cmpproject.presentation.features.dashboard

import androidx.compose.material3.*
import androidx.compose.runtime.*
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.business.cmpproject.presentation.features.dashboard.navigation.DashboardNavigator
import com.business.cmpproject.presentation.features.dashboard.navigation.DashboardTab
import org.koin.compose.koinInject
import org.jetbrains.compose.resources.painterResource

class DashboardScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<DashboardScreenModel>()
        val navigator = LocalNavigator.current!!

        DashboardContent(screenModel)
        // Logout can be added here later:
        // navigator.replace(LoginScreen())
    }
}
