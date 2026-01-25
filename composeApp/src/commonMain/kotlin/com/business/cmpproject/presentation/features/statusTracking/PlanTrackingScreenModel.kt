package com.business.cmpproject.presentation.features.statusTracking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.screenModelScope
import com.business.cmpproject.core.base.BaseScreenModel
import com.business.cmpproject.core.network.NetworkResult
import com.business.cmpproject.core.state.UiEvent
import com.business.cmpproject.core.state.UiState
import com.business.cmpproject.data.model.response.PlanRequestItem
import com.business.cmpproject.domain.repository.plan.PlanRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlanTrackingScreenModel(
    private val repo: PlanRepository
) : BaseScreenModel() {

    private val _state = MutableStateFlow<UiState<List<PlanRequestItem>>>(UiState.Loading)
    val state: StateFlow<UiState<List<PlanRequestItem>>> = _state

    private var currentPage = 1
    private var lastPage = 1
    var canLoadMore by mutableStateOf(false)
        private set

    private val allRequests = mutableListOf<PlanRequestItem>()

    init {
        loadTrackingData(isRefresh = true)
    }

    fun refresh() {
        currentPage = 1
        allRequests.clear()
        loadTrackingData(isRefresh = true)
    }

    fun loadNextPage() {
        if (currentPage < lastPage) {
            currentPage++
            loadTrackingData(isRefresh = false)
        }
    }

    private fun loadTrackingData(isRefresh: Boolean) {
        screenModelScope.launch {
            if (isRefresh) _state.value = UiState.Loading

            try {
                when (val result = repo.trackPlan(currentPage)) {
                    is NetworkResult.Success -> {
                        val paginationData = result.data
                        lastPage = paginationData.lastPage

                        // Append new data to existing list
                        allRequests.addAll(paginationData.data)

                        _state.value = UiState.Success(allRequests.toList())

                        // Check if more pages exist
                        canLoadMore = currentPage < lastPage
                    }
                    is NetworkResult.Failure -> {
                        if (isRefresh) _state.value = UiState.Error(result.error.toString())
                        sendEvent(UiEvent.ShowSnackBar(result.error.toString(), isError = true))
                    }
                }
            } catch (e: Exception) {
                if (isRefresh) _state.value = UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}