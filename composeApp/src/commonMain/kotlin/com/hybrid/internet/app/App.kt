package com.hybrid.internet.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.hybrid.internet.core.state.UiEvent
import com.hybrid.internet.core.state.UiEventBus
import com.hybrid.internet.presentation.features.login.LoginScreen
import com.hybrid.internet.presentation.features.splash.SplashScreen
import com.hybrid.internet.presentation.theme.AppTheme
import org.koin.compose.getKoin

@Composable
fun App() {

    val authEventBus = getKoin().get<UiEventBus>()

    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            Navigator(SplashScreen()) { navigator ->

                LaunchedEffect(Unit) {
                    authEventBus.events.collect { event ->
                        when (event) {
                            UiEvent.Unauthorized -> {
                                navigator.replaceAll(LoginScreen())
                            }
                            else -> {}
                        }
                    }
                }

                CurrentScreen()
            }
        }
    }
}
