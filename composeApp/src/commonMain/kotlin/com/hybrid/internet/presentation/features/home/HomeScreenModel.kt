package com.hybrid.internet.presentation.features.home

import com.hybrid.internet.core.base.BaseScreenModel
import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.core.state.UiEvent.ShowSnackBar
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.core.storage.LocalStorage
import com.hybrid.internet.data.model.response.HomeResponse
import com.hybrid.internet.data.model.response.LoginResponse
import com.hybrid.internet.domain.repository.dashboard.DashboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenModel (
    private val repo: DashboardRepository,
    private val localStorage: LocalStorage
) : BaseScreenModel() {
    private val _userData = MutableStateFlow(localStorage.getUser())
    val userData: StateFlow<LoginResponse?> = _userData
    private val _state =
        MutableStateFlow<UiState<HomeResponse>>(UiState.Loading)
    val state: StateFlow<UiState<HomeResponse>> = _state

    private var loading = false

    init { loadDashboard() }

    fun loadDashboard() {
        if (loading) return
        loading = true
        screenModelScope.launch {
            try {
                _userData.value = localStorage.getUser()
                if (_state.value !is UiState.Success) _state.value = UiState.Loading
                when (val result = repo.getDashboard()) {
                    is NetworkResult.Success -> _state.value = UiState.Success(result.data)
                    is NetworkResult.Failure -> {
                        if (_state.value !is UiState.Success) _state.value = UiState.Error(result.error.message)
                        sendEvent(ShowSnackBar(result.error.message))
                    }
                }
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Exception) {
                if (_state.value !is UiState.Success) _state.value = UiState.Error(e.message ?: "Unable to load dashboard")
            } finally {
                loading = false
            }
        }
    }
}
