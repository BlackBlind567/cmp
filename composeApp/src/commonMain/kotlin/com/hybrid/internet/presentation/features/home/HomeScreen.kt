package com.hybrid.internet.presentation.features.home

import com.hybrid.internet.presentation.features.dashboard.RegisterTabRefresh
import com.hybrid.internet.presentation.features.dashboard.HomeTab
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.presentation.components.AppScaffold
import com.hybrid.internet.presentation.components.FullScreenError
import com.hybrid.internet.presentation.features.payment.PaymentDetailsScreen
import com.hybrid.internet.presentation.features.ticketMessages.TicketMessagesScreen
import com.hybrid.internet.presentation.features.web.PaymentWebViewScreen

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeScreenModel>()
        RegisterTabRefresh(HomeTab.key, viewModel) { viewModel.loadDashboard() }
        val uiState by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
// Find the parent navigator if the current one is a TabNavigator
        val rootNavigator = navigator.parent ?: navigator
        val isDark = isSystemInDarkTheme()
        val user = viewModel.userData.collectAsState().value
        println("HomeScreen Content ==> $user")
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
                    val dashboardMessage = state.data.dashboardMessage.orEmpty()
                    val dashboardButton = state.data.dashboardButton.orEmpty()
                    val dashboardLink = state.data.dashboardLink.orEmpty()
                    var showDashboardMessage by remember(dashboardMessage) {
                        mutableStateOf(dashboardMessage.isNotBlank())
                    }

                    HomeScreenContent(
                        data = state.data,
                        isDark = isDark,
                        onTicketClick = { ticket ->
                            val ticketId = ticket.ticketId ?: ticket.id?.toString()
                            if (!ticketId.isNullOrBlank()) {
                                rootNavigator.push(
                                    TicketMessagesScreen(
                                        ticketId = ticketId,
                                        displayTicketId = ticket.ticketId ?: ticketId
                                    )
                                )
                            }
                        },
                        onInvoiceClick = { /* ... */ },
                        userName = user?.customer_name?:"",
                        userCompany = user?.company_name?:""

                    )

                    if (showDashboardMessage) {
                        DashboardMessageDialog(
                            message = dashboardMessage,
                            buttonLabel = dashboardButton,
                            onDismiss = { showDashboardMessage = false },
                            onButtonClick = {
                                showDashboardMessage = false
                                if (dashboardLink.isNotBlank()) {
                                    rootNavigator.push(
                                        PaymentWebViewScreen(
                                            url = dashboardLink,
                                            title = dashboardButton.ifBlank { "Payment" }
                                        )
                                    )
                                } else {
                                    rootNavigator.push(
                                        PaymentDetailsScreen(
                                            dueAmount = state.data.totalDueAmount
                                        )
                                    )
                                }
                            }
                        )
                    }
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

@Composable
private fun DashboardMessageDialog(
    message: String,
    buttonLabel: String,
    onDismiss: () -> Unit,
    onButtonClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Notice") },
        text = {
            Text(message)
        },
        confirmButton = {
            if (buttonLabel.isNotBlank()) {
                Button(onClick = onButtonClick) {
                    Text(buttonLabel)
                }
            } else {
                Button(onClick = onDismiss) {
                    Text("OK")
                }
            }
        },
        dismissButton = {
            if (buttonLabel.isNotBlank()) {
                TextButton(onClick = onDismiss) {
                    Text("Later")
                }
            }
        }
    )
}
