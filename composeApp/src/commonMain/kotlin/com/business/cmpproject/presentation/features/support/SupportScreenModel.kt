package com.business.cmpproject.presentation.features.support

import SupportRequest
import com.business.cmpproject.core.base.BaseScreenModel
import com.business.cmpproject.core.network.NetworkResult
import com.business.cmpproject.core.state.UiEvent.ShowSnackBar
import com.business.cmpproject.core.state.UiState
import com.business.cmpproject.core.storage.LocalStorage
import com.business.cmpproject.data.model.response.TicketData
import com.business.cmpproject.domain.repository.support.SupportRepository
import com.business.cmpproject.domain.repository.ticket.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SupportScreenModel(
    private val repo: SupportRepository,
    private val storage: LocalStorage
) : BaseScreenModel() {

    private val _state = MutableStateFlow<UiState<Any>>(UiState.Loading)
    val state: StateFlow<UiState<Any>> = _state

    fun submitReport(req: SupportRequest) {
        screenModelScope.launch {
            try {
                when (val result = repo.submitReport(req)) {

                    is NetworkResult.Success -> {
                        _state.value =
                            UiState.Success(result.data)
                    }

                    is NetworkResult.Failure -> {
                        _state.value = UiState.Error(result.error.message)
                        sendEvent(
                            ShowSnackBar(
                                message = result.error.message,
                                isError = true
                            )
                        )
                    }

                    else -> {}
                }
            } catch (e: Exception) {
                println("Dashboard API Crash: ${e.message}") // Look for this in logs!
                _state.value = UiState.Error(e.message ?: "Unknown Connection Error")
            }
        }
    }
}