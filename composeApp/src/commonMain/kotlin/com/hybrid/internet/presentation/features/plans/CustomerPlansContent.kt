package com.hybrid.internet.presentation.features.plans

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.data.model.response.PlanResponse

import com.hybrid.internet.presentation.components.FullScreenError
import com.hybrid.internet.presentation.theme.GreenPrimary
import com.hybrid.internet.presentation.theme.PinkPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerPlansContent(
    uiState: UiState<List<PlanResponse>>,
    isProcessing: Boolean,
    onRetry: () -> Unit,
    onUpdatePlan: (PlanResponse, Int?, String, Boolean) -> Unit,
    onRaiseRequest: (String, String) -> Unit // Naya callback location pass karne ke liye
) {
    val isDark = isSystemInDarkTheme()
    var selectedPlanForManage by remember { mutableStateOf<PlanResponse?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is UiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = if (isDark) PinkPrimary else GreenPrimary)
                }
            }

            is UiState.Error -> {
                FullScreenError(
                    message = uiState.message ?: "Failed to load plans",
                    onRetry = onRetry
                )
            }

            is UiState.Success -> {
                val plans = uiState.data
                if (plans.isEmpty()) {
                    FullScreenError(message = "No active plans found", onRetry = onRetry)
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(plans, key = { it.id ?: 0 }) { plan ->
                            // --- Card Click Logic ---
                            CustomerPlanCard(
                                plan = plan,
                                onManageClick = { selectedPlanForManage = it },
                                // Card ke kisi bhi hisse par click karne par form khulega
                                onCardClick = {
                                    onRaiseRequest(
                                        plan.locationId?.toString() ?: "",
                                        plan.locationName ?: ""
                                    )
                                }
                            )
                        }
                    }
                }
            }
            else -> Unit
        }

        // Bottom Sheet aur Overlay logic same rahega...
        // --- Bottom Sheet Logic ---
        selectedPlanForManage?.let { plan ->
            ManagePlanSheet(
                plan = plan,
                isDark = isDark,
                onDismiss = { selectedPlanForManage = null },
                onConfirm = { newQty, reason, isTerminate ->
                    onUpdatePlan(plan, newQty, reason, isTerminate)
                    selectedPlanForManage = null
                }
            )
        }

        // --- Premium Processing Overlay ---
        if (isProcessing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = if (isDark) PinkPrimary else GreenPrimary)
            }
        }
    }
}


