package com.hybrid.internet.presentation.features.ticketHistory

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hybrid.internet.presentation.components.AppScaffold
import org.koin.core.parameter.parametersOf

data class TicketHistoryScreen(val ticketId: Int) : Screen {

    @Composable
    override fun Content() {


        // Koin se ScreenModel mangte waqt ticketId pass karo
        val screenModel = getScreenModel<TicketHistoryScreenModel> { parametersOf(ticketId) }
        val uiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val isDark = isSystemInDarkTheme()

        AppScaffold(events = screenModel.events) {
            TicketHistoryContent(
                uiState = uiState,
                isDark = isDark,
                onBack = { navigator.pop() },
                onLoadMore = { screenModel.loadMore() },
                onRetry = { screenModel.loadMore() },
            )
        }
    }
}