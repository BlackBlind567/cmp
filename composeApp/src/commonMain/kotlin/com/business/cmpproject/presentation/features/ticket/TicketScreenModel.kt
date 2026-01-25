package com.business.cmpproject.presentation.features.ticket

import com.business.cmpproject.core.base.BaseScreenModel
import com.business.cmpproject.core.network.NetworkResult
import com.business.cmpproject.core.state.UiState
import com.business.cmpproject.core.storage.LocalStorage
import com.business.cmpproject.data.model.response.TicketData
import com.business.cmpproject.domain.repository.ticket.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TicketScreenModel(
    private val repo: TicketRepository,
    private val storage: LocalStorage
) : BaseScreenModel() {

    private val _tickets = MutableStateFlow<List<TicketData>>(emptyList())
    private val _state = MutableStateFlow<UiState<List<TicketData>>>(UiState.Loading)
    val state: StateFlow<UiState<List<TicketData>>> = _state

    // Pagination states
    private val _isPaginationLoading = MutableStateFlow(false)
    val isPaginationLoading: StateFlow<Boolean> = _isPaginationLoading

    private var currentPage = 1
    private var isLastPage = false
    private var isFetching = false

    init {
        loadTicketHistory(isRefresh = true)
    }

    fun loadTicketHistory(isRefresh: Boolean = false) {
        if (isFetching || (isLastPage && !isRefresh)) return

        if (isRefresh) {
            currentPage = 1
            isLastPage = false
            _tickets.value = emptyList()
            _state.value = UiState.Loading
        } else {
            _isPaginationLoading.value = true
        }

        isFetching = true
        screenModelScope.launch {
            try {
                // API call with page parameter
                when (val result = repo.getTicketList(page = currentPage)) {
                    is NetworkResult.Success -> {
                        val responseData = result.data // TicketPage object
                        val newTickets = responseData.data

                        if (newTickets.isNotEmpty()) {
                            _tickets.value = _tickets.value + newTickets
                            _state.value = UiState.Success(_tickets.value)

                            // Check next page existence
                            isLastPage = responseData.nextPageUrl == null
                            currentPage++
                        } else {
                            isLastPage = true
                            if (isRefresh) _state.value = UiState.Success(emptyList())
                        }
                    }
                    is NetworkResult.Failure -> {
                        _state.value = UiState.Error(result.error.message)
                    }
                }
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Unknown Error")
            } finally {
                isFetching = false
                _isPaginationLoading.value = false
            }
        }
    }
}