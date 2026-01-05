package com.business.cmpproject.presentation.features.otp

import cafe.adriel.voyager.core.model.screenModelScope
import com.business.cmpproject.core.BaseScreenModel
import com.business.cmpproject.core.network.NetworkResult
import com.business.cmpproject.core.state.UiEvent
import com.business.cmpproject.core.state.UiState
import com.business.cmpproject.core.storage.LocalStorage
import com.business.cmpproject.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OtpScreenModel(
    private val repo: AuthRepository,
    private val storage: LocalStorage
) : BaseScreenModel() {

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

    fun verifyOtp(mobile: String, otp: String) {
        screenModelScope.launch {
            _state.value = UiState.Loading

            when (val result = repo.verifyOtp(mobile, otp)) {

                is NetworkResult.Success -> {
                    // 🔐 Save everything locally
                    storage.saveToken(result.data.token)
                    storage.saveUser(result.data)

                    _state.value = UiState.Success(Unit)
                }

                is NetworkResult.Failure -> {
                    _state.value = UiState.Error(result.error.message)
                    sendEvent(UiEvent.ShowSnackBar(result.error.message))
                }
            }
        }
    }
}