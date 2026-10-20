package com.hybrid.internet.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.hybrid.internet.core.notification.NotificationNavigationBus
import com.hybrid.internet.core.notification.NotificationNavigationEvent
import com.hybrid.internet.core.state.UiEvent
import com.hybrid.internet.core.state.UiEventBus
import com.hybrid.internet.core.storage.LocalStorage
import com.hybrid.internet.presentation.features.dashboard.DashboardScreen
import com.hybrid.internet.presentation.features.login.LoginScreen
import com.hybrid.internet.presentation.features.splash.SplashScreen
import com.hybrid.internet.presentation.features.ticket.TicketHistoryList
import com.hybrid.internet.presentation.theme.AppTheme
import org.koin.compose.getKoin

@Composable
fun App() {

    val authEventBus = getKoin().get<UiEventBus>()
    val localStorage = getKoin().get<LocalStorage>()

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

                LaunchedEffect(Unit) {
                    NotificationNavigationBus.event.collect { event ->
                        when (event) {
                            is NotificationNavigationEvent.OpenTickets -> {
                                if (localStorage.getToken() == null) {
                                    navigator.replaceAll(LoginScreen())
                                } else {
                                    navigator.replaceAll(DashboardScreen())
                                    navigator.push(TicketHistoryList())
                                }
                                NotificationNavigationBus.clear()
                            }
                            null -> Unit
                        }
                    }
                }

                CurrentScreen()
            }
        }
    }
}
