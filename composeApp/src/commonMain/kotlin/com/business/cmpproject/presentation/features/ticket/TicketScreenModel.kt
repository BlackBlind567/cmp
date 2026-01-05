package com.business.cmpproject.presentation.features.ticket

import cafe.adriel.voyager.core.model.screenModelScope
import com.business.cmpproject.core.BaseScreenModel
import com.business.cmpproject.core.network.NetworkResult
import com.business.cmpproject.core.state.UiEvent
import com.business.cmpproject.core.state.UiState
import com.business.cmpproject.core.storage.LocalStorage
import com.business.cmpproject.domain.repository.AuthRepository
import com.business.cmpproject.domain.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TicketScreenModel(private val repo: TicketRepository,
                        private val storage: LocalStorage): BaseScreenModel() {



    private val _state = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val state: StateFlow<UiState<Unit>> = _state

    fun sendOtp(mobile: String) {
        screenModelScope.launch {
            when (val result = repo.sendOtp(mobile)) {
                is NetworkResult.Success -> {
                    sendEvent(UiEvent.ShowSnackBar(result.data, false))
                }
                is NetworkResult.Failure -> {
                    sendEvent(UiEvent.ShowSnackBar(result.error.message))
                }
            }
        }
    }
}