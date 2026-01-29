package com.hybrid.internet.presentation.features.changePassword

import com.hybrid.internet.core.base.BaseScreenModel
import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.core.state.UiEvent
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.data.model.request.ChangePasswordRequest
import com.hybrid.internet.domain.repository.changePassword.ChangePasswordRepository
import com.hybrid.internet.domain.repository.changePassword.ChangePasswordRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChangePasswordScreenModel(
    private val repo: ChangePasswordRepository
) : BaseScreenModel() {

    private val _state = MutableStateFlow<UiState<Int>>(UiState.Idle)
    val state: StateFlow<UiState<Int>> = _state

    fun changePassword(
        current: String,
        password: String,
        confirm: String
    ) {
        screenModelScope.launch {

            _state.value = UiState.Loading

            when (
                val result = repo.changePassword(
                    ChangePasswordRequest(
                        current_password = current,
                        password = password,
                        password_confirmation = confirm
                    )
                )
            ) {
                is NetworkResult.Success -> {
                    _state.value = UiState.Success(result.data)

                    sendEvent(
                        UiEvent.ShowSnackBar(
                            "Your password has been updated successfully!",
                            isError = false
                        )
                    )

                    sendEvent(UiEvent.NavigateBack)
                }

                is NetworkResult.Failure -> {
                    _state.value = UiState.Error(result.error.message)

                    sendEvent(
                        UiEvent.ShowSnackBar(
                            result.error.message,
                            isError = true
                        )
                    )
                }
            }
        }
    }
}

