package com.hybrid.internet.presentation.features.ticket

import SupportScreen
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.presentation.features.ticketHistory.TicketHistoryScreen
import com.hybrid.internet.presentation.theme.CreamBackground
import com.hybrid.internet.presentation.theme.DarkBackground
import com.hybrid.internet.presentation.theme.GreenPrimary
import com.hybrid.internet.presentation.theme.PinkPrimary

class TicketHistoryList : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val isDark = isSystemInDarkTheme()
        val screenModel = getScreenModel<TicketScreenModel>()

        val state by screenModel.state.collectAsState()
        val isPaginationLoading by screenModel.isPaginationLoading.collectAsState()
        val listState = rememberLazyListState()
        val navigator = LocalNavigator.currentOrThrow
        val rootNavigator = navigator.parent ?: navigator
        // Pagination Trigger: Detect when user reaches near bottom
        val shouldLoadMore = remember {
            derivedStateOf {
                val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                lastVisibleItem != null && lastVisibleItem.index >= listState.layoutInfo.totalItemsCount - 3
            }
        }

        LaunchedEffect(shouldLoadMore.value) {
            if (shouldLoadMore.value) {
                screenModel.loadTicketHistory(isRefresh = false)
            }
        }

        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = { rootNavigator.push(SupportScreen()) },
                    containerColor = if (isDark) PinkPrimary else GreenPrimary,
                    contentColor = Color.White,
                    icon = { Icon(Icons.Default.SupportAgent, contentDescription = null) },
                    text = { Text("Support") }
                )
            },
            containerColor = if (isDark) DarkBackground else CreamBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { padding ->
            Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                when (val current = state) {
                    is UiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = GreenPrimary
                        )
                    }

                    is UiState.Error -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(current.message, color = Color.Red)
                            Button(onClick = { screenModel.loadTicketHistory(true) }) {
                                Text("Retry")
                            }
                        }
                    }

                    is UiState.Success -> {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(12.dp)
                        ) {
                            items(current.data) { ticket ->
                                TicketHistoryContent(ticket, isDark, onTicketClick = { id ->
                                    rootNavigator.push(TicketHistoryScreen(id))
                                })
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            // FOOTER LOADER: Only shows when fetching NEXT page
                            if (isPaginationLoading) {
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(32.dp),
                                            color = GreenPrimary,
                                            strokeWidth = 3.dp
                                        )
                                    }
                                }
                            }
                        }
                    }
                    else -> { /* Handle Idle or other states */ }
                }
            }
        }
    }
}

