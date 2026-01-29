package com.hybrid.internet.presentation.features.support

import SupportRequest
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hybrid.internet.core.state.UiEvent
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.data.model.response.PlanResponse
import com.hybrid.internet.presentation.components.AppScaffold
import com.hybrid.internet.presentation.features.plans.CustomerPlansScreenModel

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

        AppScaffold(events = screenModel.events) {

            when (planState) {

                is UiState.Loading -> {
                    // Optional loader
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
                    Text("Failed to load plans")
                }

                else -> Unit
            }
        }
    }
}

