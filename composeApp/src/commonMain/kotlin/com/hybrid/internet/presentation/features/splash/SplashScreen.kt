package com.hybrid.internet.presentation.features.splash

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.hybrid.internet.presentation.features.dashboard.DashboardScreen
import com.hybrid.internet.presentation.features.login.LoginScreen

class SplashScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<SplashScreenModel>()
        val navigator = LocalNavigator.current!!

        SplashContent(viewModel) { isLoggedIn ->
            if (isLoggedIn) {
              navigator.replace(DashboardScreen())
            } else {
                navigator.replace(LoginScreen())
            }
        }
    }
}