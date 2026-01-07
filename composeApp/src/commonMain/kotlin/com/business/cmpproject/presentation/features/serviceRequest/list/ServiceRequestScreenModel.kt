package com.business.cmpproject.presentation.features.serviceRequest.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.business.cmpproject.core.BaseScreenModel
import com.business.cmpproject.core.network.NetworkResult
import com.business.cmpproject.core.state.UiState
import com.business.cmpproject.data.model.response.ServiceRequestItem
import com.business.cmpproject.domain.repository.serviceRequest.ServiceRequestRepository
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

    init { loadRequests(isRefresh = true) }

    fun loadRequests(isRefresh: Boolean = true) {
        if (isRefresh) {
            currentPage = 1
            allRequests.clear()
        }

        screenModelScope.launch {
            if (isRefresh) _state.value = UiState.Loading

            when (val result = repo.getServiceRequests(currentPage)) {
                is NetworkResult.Success -> {
                    lastPage = result.data.lastPage
                    allRequests.addAll(result.data.requests)
                    _state.value = UiState.Success(allRequests.toList())
                    canLoadMore = currentPage < lastPage
                }
                is NetworkResult.Failure -> {
                    if (isRefresh) _state.value = UiState.Error(result.error.toString())
                }
            }
        }
    }

    fun loadMore() {
        if (currentPage < lastPage) {
            currentPage++
            loadRequests(isRefresh = false)
        }
    }
}