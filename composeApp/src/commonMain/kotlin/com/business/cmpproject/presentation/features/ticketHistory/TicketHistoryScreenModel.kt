package com.business.cmpproject.presentation.features.ticketHistory

import com.business.cmpproject.core.base.BaseScreenModel
import com.business.cmpproject.core.network.NetworkResult
import com.business.cmpproject.core.state.UiState
import com.business.cmpproject.data.model.response.TicketHistoryItem
import com.business.cmpproject.domain.repository.ticket.TicketDetailsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TicketHistoryScreenModel(
    private val repo: TicketDetailsRepository, // Naming convention synced
    private val ticketId: Int // Passed from Screen
) : BaseScreenModel() {

    private val _state = MutableStateFlow<UiState<List<TicketHistoryItem>>>(UiState.Loading)
    val state = _state.asStateFlow()

    private var currentPage = 1
    private var lastPage = 1
    private val allHistory = mutableListOf<TicketHistoryItem>()

    init {
        loadHistory(isRefresh = true)
    }

    fun loadHistory(isRefresh: Boolean = true) {
        if (isRefresh) {
            currentPage = 1
            allHistory.clear()
        }

        screenModelScope.launch {
            if (isRefresh) _state.value = UiState.Loading

            // API key mapping logic
            val myKeys = mapOf(
                "ticket_id" to ticketId.toString()
            )

            try {
                when (val result = repo.fetchTicketHistory(currentPage, myKeys)) {
                    is NetworkResult.Success -> {
                        val paginationData = result.data
                        lastPage = paginationData.lastPage
                        allHistory.addAll(paginationData.data)
                        _state.value = UiState.Success(allHistory.toList())
                    }
                    is NetworkResult.Failure -> {
                        if (isRefresh) _state.value = UiState.Error(result.error.toString())
                    }
                }
            } catch (e: Exception) {
                if (isRefresh) _state.value = UiState.Error(e.message ?: "Network Error")
            }
        }
    }

    fun loadMore() {
        if (currentPage < lastPage) {
            currentPage++
            loadHistory(isRefresh = false)
        }
    }
}