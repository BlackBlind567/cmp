package com.hybrid.internet.presentation.features.login


import com.hybrid.internet.core.base.BaseScreenModel
import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.core.session.SessionManager
import com.hybrid.internet.core.state.UiEvent.*
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.core.state.UiState.*
import com.hybrid.internet.core.storage.LocalStorage
import com.hybrid.internet.data.model.request.LoginRequest
import com.hybrid.internet.domain.repository.login.AuthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginScreenModel(
    private val repo: AuthRepository,
    private val storage: LocalStorage,
    private val sessionManager: SessionManager
) : BaseScreenModel() {

    private val _state = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val state: StateFlow<UiState<Unit>> = _state

    fun login(email: String, password: String) {
        screenModelScope.launch {

            _state.value = UiState.Loading

            when (val result = repo.login(LoginRequest(email, password))) {

                is NetworkResult.Success -> {
                    storage.saveToken(result.data.token)
                    _state.value = Success(Unit)

                    sendEvent(
                        ShowSnackBar(
                            message = "Login successful",
                            isError = false
                        )
                    )
                }

                is NetworkResult.Failure -> {
                    sessionManager.handle(result.error)
                    _state.value = Error(result.error.message)

                    sendEvent(
                        ShowSnackBar(
                            message = result.error.message,
                            isError = true
                        )
                    )
                }


            }
        }
    }
}
