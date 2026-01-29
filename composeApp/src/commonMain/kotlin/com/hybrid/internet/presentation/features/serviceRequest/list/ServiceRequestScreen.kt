package com.hybrid.internet.presentation.features.serviceRequest.list

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hybrid.internet.presentation.components.AppScaffold
import com.hybrid.internet.presentation.features.serviceRequest.add.RaiseServiceRequestScreen

class ServiceRequestScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<ServiceRequestScreenModel>()
        val uiState by screenModel.state.collectAsState()
        val isDark = isSystemInDarkTheme()
        val rootNavigator = navigator.parent ?: navigator
        AppScaffold(events = screenModel.events) {
            ServiceRequestContent(
                uiState = uiState,
                isDark = isDark,
                canLoadMore = screenModel.canLoadMore,
                onBack = { navigator.pop() },
                onLoadMore = { screenModel.loadMore() },
                onNewRequest = {  rootNavigator.push(
                    RaiseServiceRequestScreen(
                        onRefresh = { screenModel.loadRequests(isRefresh = true) } // Reload logic
                    )
                ) }
            )
        }
    }
}