package com.hybrid.internet.presentation.features.ticket

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.hybrid.internet.core.base.BaseScreenModel
import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.core.state.UiEvent
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.core.storage.LocalStorage
import com.hybrid.internet.data.model.response.TicketData
import com.hybrid.internet.domain.repository.ticket.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TicketScreenModel(
    private val repo: TicketRepository,
    private val storage: LocalStorage
) : BaseScreenModel() {

    private val _state =
        MutableStateFlow<UiState<List<TicketData>>>(UiState.Loading)
    val state: StateFlow<UiState<List<TicketData>>> = _state

    private var currentPage = 1
    private var lastPage: Int = 1

    var canLoadMore by mutableStateOf(false)
        private set

    private val allTickets = mutableListOf<TicketData>()

    init {
        loadTickets(isRefresh = true)
    }

    fun refresh() {
        println("inside refresh")
        currentPage = 1
        allTickets.clear()
        loadTickets(isRefresh = true)
    }

    fun loadNextPage() {
        if (currentPage < lastPage) {
            currentPage++
            loadTickets(isRefresh = false)
        }
    }

    private fun loadTickets(isRefresh: Boolean) {
        screenModelScope.launch {

            if (isRefresh) {
                _state.value = UiState.Loading
            }

            try {
                when (val result = repo.getTicketList(page = currentPage)) {

                    is NetworkResult.Success -> {
                        val pageData = result.data

                        lastPage = pageData.lastPage!!
                        allTickets.addAll(pageData.data)

                        // 🔥 NEW list reference every time
                        _state.value = UiState.Success(allTickets.toList())

                        canLoadMore = currentPage < lastPage
                    }

                    is NetworkResult.Failure -> {
                        if (isRefresh) {
                            _state.value =
                                UiState.Error(result.error.message)
                        }

                        sendEvent(
                            UiEvent.ShowSnackBar(
                                result.error.message ?: "Failed to load tickets",
                                isError = true
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                if (isRefresh) {
                    _state.value =
                        UiState.Error(e.message ?: "Unknown Error")
                }
            }
        }
    }
}
