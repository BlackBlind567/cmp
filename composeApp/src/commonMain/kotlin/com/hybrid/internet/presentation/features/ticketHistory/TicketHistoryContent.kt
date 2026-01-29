package com.hybrid.internet.presentation.features.ticketHistory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.core.state.UiStatePagination
import com.hybrid.internet.data.model.response.TicketHistoryItem
import com.hybrid.internet.presentation.components.FullScreenError
import com.hybrid.internet.presentation.components.StandardTopAppBar
import com.hybrid.internet.presentation.theme.CreamBackground
import com.hybrid.internet.presentation.theme.DarkBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketHistoryContent(
    uiState: UiState<List<TicketHistoryItem>>,
    isDark: Boolean,
    onBack: () -> Unit,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = if (isDark) DarkBackground else CreamBackground, // Blue-ish Shade
        topBar = {
            StandardTopAppBar(
                title = "Ticket Timeline",
                subtitle = "History logs",
                showBack = true,
                onBack = onBack,
                isTitleCenter = false,
                scrollBehavior = scrollBehavior
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (uiState) {
                is UiState.Loading -> { /* Premium Shimmer */ }
                is UiState.Success -> {
                    if (uiState.data.isEmpty()) {
                        FullScreenError(message = "No active plans found", onRetry = onRetry)
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            itemsIndexed(uiState.data) { index, item ->
                                TicketHistoryTimelineItem(
                                    item = item,
                                    isLast = index == uiState.data.size - 1,
                                    isDark = isDark
                                )

                                // Infinite scroll trigger
                                if (index == uiState.data.size - 1) {
                                    LaunchedEffect(Unit) { onLoadMore() }
                                }
                            }
                        }
                    }
                }
                is UiState.Error -> { /* Error Layout */ }
                UiState.Idle -> TODO()
                is UiStatePagination.Error -> TODO()
                UiStatePagination.Idle -> TODO()
                UiStatePagination.Loading -> TODO()
                is UiStatePagination.Success<*> -> TODO()
            }
        }
    }
}