package com.business.cmpproject.presentation.features.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.business.cmpproject.core.state.UiState
import com.business.cmpproject.presentation.components.AppScaffold
import com.business.cmpproject.presentation.components.FullScreenError

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeScreenModel>()
        val uiState by viewModel.state.collectAsState()

        AppScaffold(events = viewModel.events) {
            // Use a 'when' block to safely handle states
            when (val state = uiState) {
                is UiState.Loading -> {
                    // Show a loader while the API is calling
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Success -> {
                    HomeScreenContent(
                        state = state.data,
                        onViewAllTickets = { /* ... */ },
                        onTicketClick = { /* ... */ },
                        onViewAllInvoices = { /* ... */ },
                        onInvoiceClick = { /* ... */ }
                    )
                }
                is UiState.Error -> {
                    // Show an error message or a retry button
                    FullScreenError(
                        message = state.message,
                        onRetry = { viewModel.loadDashboard() }
                    )
                }

                UiState.Idle -> TODO()
            }
        }
    }
}
