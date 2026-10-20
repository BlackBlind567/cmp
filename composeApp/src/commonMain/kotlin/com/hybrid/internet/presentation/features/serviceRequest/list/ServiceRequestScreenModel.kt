package com.hybrid.internet.presentation.features.serviceRequest.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.hybrid.internet.core.base.BaseScreenModel
import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.data.model.response.ServiceRequestItem
import com.hybrid.internet.domain.repository.serviceRequest.ServiceRequestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ServiceRequestScreenModel(
    private val repo: ServiceRequestRepository
) : BaseScreenModel() {

    private val _state = MutableStateFlow<UiState<List<ServiceRequestItem>>>(UiState.Loading)
    val state = _state.asStateFlow()

    private var currentPage = 1
    private var lastPage = 1
    var canLoadMore by mutableStateOf(false)
    private val allRequests = mutableListOf<ServiceRequestItem>()

    private var loading = false

    init { loadRequests() }

    fun loadRequests(isRefresh: Boolean = true) {
        if (loading) return
        loading = true
        screenModelScope.launch {
            try {
                if (_state.value !is UiState.Success) _state.value = UiState.Loading
                val refreshed = if (isRefresh) mutableListOf<ServiceRequestItem>() else allRequests.toMutableList()
                var page = if (isRefresh) 1 else currentPage + 1
                val targetPage = if (isRefresh) currentPage else page
                var newLastPage = lastPage
                var loadedPage = currentPage
                while (page <= targetPage) {
                    when (val result = repo.getServiceRequests(page)) {
                        is NetworkResult.Success -> {
                            newLastPage = result.data.lastPage
                            refreshed.addAll(result.data.requests)
                            loadedPage = page
                        }
                        is NetworkResult.Failure -> {
                            if (_state.value !is UiState.Success) _state.value = UiState.Error(result.error.message)
                            sendEvent(com.hybrid.internet.core.state.UiEvent.ShowSnackBar(result.error.message))
                            return@launch
                        }
                    }
                    if (page >= newLastPage) break
                    page++
                }
                allRequests.clear()
                allRequests.addAll(refreshed)
                currentPage = loadedPage
                lastPage = newLastPage
                canLoadMore = currentPage < lastPage
                _state.value = UiState.Success(allRequests.toList())
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Exception) {
                if (_state.value !is UiState.Success) _state.value = UiState.Error(e.message ?: "Unable to load requests")
            } finally {
                loading = false
            }
        }
    }

    fun loadMore() {
        if (currentPage < lastPage) loadRequests(isRefresh = false)
    }
}
