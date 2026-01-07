package com.business.cmpproject.presentation.features.plans

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.business.cmpproject.presentation.components.AppScaffold
import com.business.cmpproject.presentation.components.StandardTopAppBar
import com.business.cmpproject.presentation.features.serviceRequest.add.RaiseServiceRequestScreen
import com.business.cmpproject.presentation.features.statusTracking.PlanTrackingScreen

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
