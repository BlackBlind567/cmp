package com.hybrid.internet.presentation.features.plans

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hybrid.internet.presentation.components.AppScaffold
import com.hybrid.internet.presentation.features.serviceRequest.add.RaiseServiceRequestScreen

class CustomerPlansScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<CustomerPlansScreenModel>()
        val uiState by screenModel.state.collectAsState()
        val isProcessing by screenModel.isProcessing.collectAsState()

        // Premium Scroll Behavior
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        val navigator = LocalNavigator.currentOrThrow
        val rootNavigator = navigator.parent ?: navigator

        AppScaffold(events = screenModel.events) {
            CustomerPlansContent(
                uiState = uiState,
                isProcessing = isProcessing,
                onRetry = { screenModel.loadPlans() },
                onUpdatePlan = { plan, qty, reason, isTerminate ->
                    screenModel.processPlanUpdate(plan, qty, reason, isTerminate)
                },
                onRaiseRequest = { locId, locName ->
                    // Yahan hum naye screen par bhej rahe hain data ke sath
                    rootNavigator.push(
                        RaiseServiceRequestScreen(
                            locationId = locId,
                            locationName = locName
                        )
                    )
                }
            )
            }
        }
    }
