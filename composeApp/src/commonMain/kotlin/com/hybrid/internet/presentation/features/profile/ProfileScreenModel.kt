package com.hybrid.internet.presentation.features.profile

import com.hybrid.internet.core.base.BaseScreenModel
import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.core.state.UiEvent
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.core.storage.LocalStorage
import com.hybrid.internet.data.model.response.LoginResponse
import com.hybrid.internet.domain.repository.login.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileScreenModel(
    private val localStorage: LocalStorage,
    private val repo: AuthRepository
) : BaseScreenModel() {

    private val _state = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val state: StateFlow<UiState<Unit>> = _state

    private val _userData =
        MutableStateFlow(localStorage.getUser())
    val userData: StateFlow<LoginResponse?> = _userData

    fun logout() {
        screenModelScope.launch {

            _state.value = UiState.Loading

            when (val result = repo.logout()) {

                is NetworkResult.Success -> {
                    localStorage.clear()

                    _state.value = UiState.Success(Unit)

                    sendEvent(
                        UiEvent.ShowSnackBar(
                            "Logout successfully",
                            isError = false
                        )
                    )

                    sendEvent(UiEvent.NavigateToLogin)
                }

                is NetworkResult.Failure -> {
                    _state.value = UiState.Error(result.error.message)

                    sendEvent(
                        UiEvent.ShowSnackBar(
                            result.error.message ?: "Logout failed"
                        )
                    )
                }
            }
        }
    }
}
