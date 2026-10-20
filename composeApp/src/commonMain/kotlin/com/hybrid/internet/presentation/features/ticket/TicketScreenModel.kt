package com.hybrid.internet.presentation.features.ticket

import com.hybrid.internet.data.model.request.TicketConfirmationRequest
import kotlinx.coroutines.CancellationException

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

    var confirmationTicket by mutableStateOf<TicketData?>(null)
        private set
    var isConfirming by mutableStateOf(false)
        private set
    var confirmationError by mutableStateOf<String?>(null)
        private set
    var confirmationMessage by mutableStateOf<String?>(null)
        private set

    fun openConfirmation(ticket: TicketData) {
        if (isConfirming || !ticket.status.orEmpty().trim().equals("Pending Confirmation", ignoreCase = true) || ticket.ticketId.isNullOrBlank()) return
        confirmationError = null
        confirmationTicket = ticket
    }

    fun dismissConfirmation() {
        if (!isConfirming) confirmationTicket = null
    }

    fun clearConfirmationMessage() { confirmationMessage = null }

    fun confirmTicket(resolved: Boolean, rating: Int, remark: String) {
        val ticket = confirmationTicket ?: return
        val ticketId = ticket.ticketId ?: return
        if (isConfirming) return
        if (resolved && rating !in 1..5) {
            confirmationError = "Please select a rating from 1 to 5 stars."
            return
        }
        isConfirming = true
        confirmationError = null
        screenModelScope.launch {
            try {
                val request = TicketConfirmationRequest(
                    ticketId, if (resolved) "accept" else "reject",
                    if (resolved) rating else null, remark.trim()
                )
                when (val result = repo.confirmTicket(request)) {
                    is NetworkResult.Success -> {
                        val index = allTickets.indexOfFirst { it.ticketId == ticketId }
                        if (index >= 0) allTickets[index] = allTickets[index].copy(status = result.data.status)
                        _state.value = UiState.Success(allTickets.toList())
                        confirmationTicket = null
                        confirmationMessage = if (resolved) "Resolution confirmed. Thank you for your feedback." else "Feedback submitted. Your issue is still unresolved."
                    }
                    is NetworkResult.Failure -> confirmationError = result.error.message
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                confirmationError = "Unable to submit feedback. Please try again."
            } finally {
                isConfirming = false
            }
        }
    }

    private var loading = false

    init { refresh() }

    fun refresh() = loadTickets(isRefresh = true)

    fun loadNextPage() {
        if (currentPage < lastPage) loadTickets(isRefresh = false)
    }

    private fun loadTickets(isRefresh: Boolean) {
        if (loading) return
        loading = true
        screenModelScope.launch {
            try {
                if (_state.value !is UiState.Success) _state.value = UiState.Loading
                val refreshed = if (isRefresh) mutableListOf<TicketData>() else allTickets.toMutableList()
                var page = if (isRefresh) 1 else currentPage + 1
                val targetPage = if (isRefresh) currentPage else page
                var newLastPage = lastPage
                var loadedPage = currentPage
                while (page <= targetPage) {
                    when (val result = repo.getTicketList(page)) {
                        is NetworkResult.Success -> {
                            newLastPage = result.data.lastPage ?: 1
                            refreshed.addAll(result.data.data)
                            loadedPage = page
                        }
                        is NetworkResult.Failure -> {
                            if (_state.value !is UiState.Success) _state.value = UiState.Error(result.error.message)
                            sendEvent(UiEvent.ShowSnackBar(result.error.message))
                            return@launch
                        }
                    }
                    if (page >= newLastPage) break
                    page++
                }
                // Commit only once all requested pages succeeded; failed refreshes retain the old list.
                allTickets.clear()
                allTickets.addAll(refreshed)
                currentPage = loadedPage
                lastPage = newLastPage
                canLoadMore = currentPage < lastPage
                _state.value = UiState.Success(allTickets.toList())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (_state.value !is UiState.Success) _state.value = UiState.Error(e.message ?: "Unable to load tickets")
            } finally {
                loading = false
            }
        }
    }
}
