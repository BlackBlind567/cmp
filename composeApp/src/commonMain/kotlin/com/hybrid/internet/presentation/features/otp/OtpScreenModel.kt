package com.hybrid.internet.presentation.features.otp

import com.hybrid.internet.core.base.BaseScreenModel
import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.core.state.UiEvent
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.core.storage.LocalStorage
import com.hybrid.internet.domain.repository.login.AuthRepository
import kotlinx.coroutines.delay
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
            val result = repo.sendOtp(mobile)

            when (result) {
                is NetworkResult.Success -> {
                    delay(1000)
                    sendEvent(UiEvent.ShowSnackBar(result.data, false))
                }
                is NetworkResult.Failure -> {
                    println("OTP SENT Json ==> ${result.error.message}")
                    delay(1000)
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