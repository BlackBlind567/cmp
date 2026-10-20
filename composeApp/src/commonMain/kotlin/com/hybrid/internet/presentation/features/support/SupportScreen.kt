package com.hybrid.internet.presentation.features.support

import SupportRequest
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hybrid.internet.core.state.UiEvent
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.data.model.response.PlanResponse
import com.hybrid.internet.presentation.components.AppScaffold
import com.hybrid.internet.presentation.components.FullScreenError
import com.hybrid.internet.presentation.features.plans.CustomerPlansScreenModel
import kotlinx.coroutines.flow.merge

class SupportScreen( val onRefresh: () -> Unit = {}) : Screen {

    @Composable
    override fun Content() {

        val screenModel = getScreenModel<SupportScreenModel>()
        val plansModel = getScreenModel<CustomerPlansScreenModel>()
        val submitState by screenModel.state.collectAsState()
        val planState by plansModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val isDark = isSystemInDarkTheme()
        // ✅ Handle one-time UI events here
        LaunchedEffect(Unit) {
            screenModel.events.collect { event ->
                when (event) {

                    UiEvent.NavigateBack -> {
                        println("Navigate back")
                        onRefresh()
                        navigator.pop()
                    }
                    else -> Unit
                }
            }
        }

        val events = remember(screenModel.events, plansModel.events) {
            merge(screenModel.events, plansModel.events)
        }

        AppScaffold(events = events) {

            when (planState) {

                is UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Success -> {
                    val plans =
                        (planState as UiState.Success<List<PlanResponse>>).data

                    SupportContent(
                        locationResp = plans,
                        isSubmitting = submitState is UiState.Loading,
                        onSubmit = { payload ->
                            screenModel.submitReport(
                                SupportRequest(
                                    altMobile = payload["alt_mobile"]?.toString().orEmpty(),
                                    altEmail = payload["email"]?.toString().orEmpty(),
                                    category = payload["category"]?.toString().orEmpty(),
                                    subCategory = payload["sub_category"]?.toString().orEmpty(),
                                    location = payload["location"]?.toString().orEmpty(),
                                    remark = payload["remark"]?.toString().orEmpty(),
                                    message = payload["message"]?.toString().orEmpty(),
                                    image = payload["image"]?.toString().orEmpty()
                                )
                            )
                        },
                        onBack = {
                            navigator.pop()
                        },
                        isDark = isDark
                    )
                }

                is UiState.Error -> {
                    val message = (planState as UiState.Error).message
                    FullScreenError(
                        message = message.ifBlank { "Failed to load plans" },
                        onRetry = { plansModel.loadPlans() }
                    )
                }

                else -> Unit
            }
        }
    }
}

