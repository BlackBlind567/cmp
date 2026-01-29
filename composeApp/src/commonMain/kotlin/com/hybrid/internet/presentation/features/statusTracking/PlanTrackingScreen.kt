package com.hybrid.internet.presentation.features.statusTracking

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.presentation.components.AppScaffold
import com.hybrid.internet.presentation.components.StandardTopAppBar

class PlanTrackingScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val isDark = isSystemInDarkTheme()
        val screenModel = getScreenModel<PlanTrackingScreenModel>()
        val uiState by screenModel.state.collectAsState()

        AppScaffold(events = screenModel.events) {
            Column{

                StandardTopAppBar(
                    title = "Request Tracking",
                    subtitle = "Monitor your plan changes",
                    showBack = true,
                    onBack = { navigator.pop() },
                    isTitleCenter = false,
                    menuItems = listOf("Refresh" to { screenModel.refresh() })
                )

                when (val state = uiState) {
                    is UiState.Loading -> { /* Shimmer or Loader */ }
                    is UiState.Success -> {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(state.data) { request ->
                                PlanTrackingContent(request, isDark)
                            }

                            // Pagination Trigger
                            item {
                                if (screenModel.canLoadMore) {
                                    LaunchedEffect(Unit) { screenModel.loadNextPage() }
                                    Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator(strokeWidth = 2.dp)
                                    }
                                }
                            }
                        }
                    }
                    is UiState.Error -> { /* Error View */ }
                    UiState.Idle -> TODO()
                    else -> {}
                }
            }
        }
    }
}