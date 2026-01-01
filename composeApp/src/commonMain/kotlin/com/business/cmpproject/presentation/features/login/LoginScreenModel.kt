package com.business.cmpproject.presentation.features.login


import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.business.cmpproject.core.BaseScreenModel
import com.business.cmpproject.core.network.NetworkResult
import com.business.cmpproject.core.session.SessionManager
import com.business.cmpproject.core.state.UiEvent
import com.business.cmpproject.core.state.UiEvent.*
import com.business.cmpproject.core.state.UiState
import com.business.cmpproject.core.state.UiState.*
import com.business.cmpproject.core.storage.LocalStorage
import com.business.cmpproject.data.model.request.LoginRequest
import com.business.cmpproject.domain.repository.AuthRepository
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
