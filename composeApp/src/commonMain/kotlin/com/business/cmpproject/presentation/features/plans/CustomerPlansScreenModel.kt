package com.business.cmpproject.presentation.features.plans

import com.business.cmpproject.core.BaseScreenModel
import com.business.cmpproject.core.network.NetworkResult
import com.business.cmpproject.core.state.UiEvent
import com.business.cmpproject.core.state.UiState
import com.business.cmpproject.data.model.request.PlanRequest
import com.business.cmpproject.data.model.response.PlanResponse
import com.business.cmpproject.domain.repository.plan.PlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CustomerPlansScreenModel(
    private val repo: PlanRepository
) : BaseScreenModel() {

    // Using your UiState pattern with the PlanListResponse model
    private val _state = MutableStateFlow<UiState<List<PlanResponse>>>(UiState.Loading)
    val state: StateFlow<UiState<List<PlanResponse>>> = _state

    // State for the termination overlay loader
    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    init {
        loadPlans()
    }

    fun loadPlans() {
        screenModelScope.launch {
            _state.value = UiState.Loading
            try {
                // No pagination, just calling the API
                when (val result = repo.getCustomerPlan()) {
                    is NetworkResult.Success -> {
                        _state.value = UiState.Success(result.data)
                    }
                    is NetworkResult.Failure -> {
                        _state.value = UiState.Error(result.error.message)
                        sendEvent(
                            UiEvent.ShowSnackBar(
                                message = result.error.message,
                                isError = true
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Unknown Connection Error")
            }
        }
    }

    fun processPlanUpdate(
        plan: PlanResponse,
        newQty: Int?,
        reason: String,
        isTermination: Boolean = false
    ) {
        val currentQty = plan.qty ?: 0

        // Auto-selection Logic
        val actionType = when {
            isTermination -> "terminate"
            newQty != null && newQty > currentQty -> "upgrade"
            newQty != null && newQty < currentQty -> "downgrade"
            else -> return // Same value or invalid
        }

        screenModelScope.launch {
            _isProcessing.value = true
            try {
                val request = PlanRequest(
                    planId = plan.id!!,
                    reason = reason,
                    actionType = actionType,
                    newQty = newQty
                )

                when (val result = repo.updatePlan(request)) {
                    is NetworkResult.Success -> {
                        sendEvent(UiEvent.ShowSnackBar("${actionType.uppercase()} request sent!"))
                        loadPlans() // Refresh the list
                    }
                    is NetworkResult.Failure -> {
                        sendEvent(UiEvent.ShowSnackBar(result.error.toString(), isError = true))
                    }
                }
            } catch (e: Exception) {
                sendEvent(UiEvent.ShowSnackBar(e.message ?: "Process failed", isError = true))
            } finally {
                _isProcessing.value = false
            }
        }
    }
}