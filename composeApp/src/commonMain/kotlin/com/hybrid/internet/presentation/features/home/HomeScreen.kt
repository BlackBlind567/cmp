package com.hybrid.internet.presentation.features.home

import androidx.compose.foundation.isSystemInDarkTheme
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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.presentation.components.AppScaffold
import com.hybrid.internet.presentation.components.FullScreenError
import com.hybrid.internet.presentation.features.invoice.InvoiceListScreen

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeScreenModel>()
        val uiState by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
// Find the parent navigator if the current one is a TabNavigator
        val rootNavigator = navigator.parent ?: navigator
        val isDark = isSystemInDarkTheme()
        val user = viewModel.userData.collectAsState().value
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
                        data = state.data,
                        isDark = isDark,
                        onViewAllTickets = { /* ... */ },
                        onTicketClick = {  },
                        onViewAllInvoices = {
                            // Use rootNavigator so it pushes a Screen, not a Tab
                            rootNavigator.push(InvoiceListScreen(
                                invoices = state.data.recentInvoices!!
                            ))
                        },
                        onInvoiceClick = { /* ... */ },
                        userName = user?.customer_name?:"",
                        userCompany = user?.company_name?:""

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
                else -> {}
            }
        }
    }
}
