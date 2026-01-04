package com.business.cmpproject.presentation.features.plans

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
import com.business.cmpproject.core.state.UiState
import com.business.cmpproject.presentation.components.AppScaffold
import com.business.cmpproject.presentation.components.FullScreenError
import com.business.cmpproject.presentation.theme.GreenPrimary
import com.business.cmpproject.presentation.theme.PinkPrimary

class CustomerPlansScreen : Screen {

    @Composable
    override fun Content() {
        // Orchestration layer
        val screenModel = getScreenModel<CustomerPlansScreenModel>()
        val uiState by screenModel.state.collectAsState()
        val isProcessing by screenModel.isProcessing.collectAsState()

        AppScaffold(events = screenModel.events) {
            CustomerPlansContent(
                uiState = uiState,
                isProcessing = isProcessing,
                onRetry = { screenModel.loadPlans() },
                onUpdatePlan = { plan, qty, reason, isTerminate ->
                    screenModel.processPlanUpdate(plan, qty, reason, isTerminate)
                }
            )
        }
    }
}
