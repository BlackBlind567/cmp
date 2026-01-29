package com.hybrid.internet.presentation.features.support

import SupportRequest
import com.hybrid.internet.core.base.BaseScreenModel
import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.core.state.UiEvent
import com.hybrid.internet.core.state.UiEvent.ShowSnackBar
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.core.storage.LocalStorage
import com.hybrid.internet.data.model.response.SupportTicketResponse
import com.hybrid.internet.domain.repository.support.SupportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SupportScreenModel(
    private val repo: SupportRepository,
    private val storage: LocalStorage
) : BaseScreenModel() {

    private val _state =
        MutableStateFlow<UiState<SupportTicketResponse>>(UiState.Idle)
    val state: StateFlow<UiState<SupportTicketResponse>> = _state

    fun submitReport(req: SupportRequest) {
        screenModelScope.launch {
            _state.value = UiState.Loading

            when (val result = repo.submitReport(req)) {

                is NetworkResult.Success -> {
                    _state.value = UiState.Success(result.data)

                    // 🔔 Tell UI to go back
                    sendEvent(UiEvent.NavigateBack)
                }

                is NetworkResult.Failure -> {
                    _state.value = UiState.Error(result.error.message)

                    sendEvent(
                        UiEvent.ShowSnackBar(
                            message = result.error.message
                                ?: "Failed to submit request",
                            isError = true
                        )
                    )
                }
            }
        }
    }
}
