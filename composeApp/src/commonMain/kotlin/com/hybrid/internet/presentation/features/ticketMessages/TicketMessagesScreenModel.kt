package com.hybrid.internet.presentation.features.ticketMessages

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.hybrid.internet.core.base.BaseScreenModel
import com.hybrid.internet.core.base.encodeImageDataUri
import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.core.network.HttpError
import com.hybrid.internet.core.state.UiEvent
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.data.model.response.TicketMessageItem
import com.hybrid.internet.domain.repository.ticket.TicketRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TicketMessagesScreenModel(
    private val repo: TicketRepository,
    private val ticketId: String
) : BaseScreenModel() {

    private val _state =
        MutableStateFlow<UiState<List<TicketMessageItem>>>(UiState.Loading)
    val state: StateFlow<UiState<List<TicketMessageItem>>> = _state

    private val _isSending = MutableStateFlow(false)
    val isSending: StateFlow<Boolean> = _isSending

    private val sentPhotos = SentPhotoMessages()

    init {
        startPolling()
    }

    fun loadMessages(showLoading: Boolean = true) {
        screenModelScope.launch {
            fetchMessages(showLoading = showLoading, showError = true)
        }
    }

    var draft by mutableStateOf("")
        private set
    var attachment by mutableStateOf<ByteArray?>(null)
        private set
    var closedTicketMessage by mutableStateOf<String?>(null)
        private set

    fun updateDraft(value: String) {
        if (!_isSending.value && closedTicketMessage == null) draft = value
    }

    fun attachImage(bytes: ByteArray?) {
        if (!_isSending.value && closedTicketMessage == null) attachment = bytes
    }

    fun sendMessage() {
        val cleanMessage = draft.trim()
        val selectedImage = attachment
        if (closedTicketMessage != null || (cleanMessage.isBlank() && selectedImage == null) || _isSending.value) return
        _isSending.value = true
        screenModelScope.launch {
            try {
                val image = selectedImage?.let { bytes ->
                    withContext(Dispatchers.Default) { encodeImageDataUri(bytes) }
                }
                image?.let { encoded ->
                    withContext(Dispatchers.Default) {
                        val chunkSize = 3000
                        val totalChunks = (encoded.length + chunkSize - 1) / chunkSize
//                        println("CHAT_IMAGE_BASE64 BEGIN ticket=$ticketId chars=${encoded.length} chunks=$totalChunks")
                        var offset = 0
                        var chunk = 1
                        while (offset < encoded.length) {
                            val end = minOf(offset + chunkSize, encoded.length)
                            println(
                                "CHAT_IMAGE_BASE64 [$chunk/$totalChunks] ${
                                    encoded.substring(
                                        offset,
                                        end
                                    )
                                }"
                            )
                            offset = end
                            chunk++
                        }
                        println("CHAT_IMAGE_BASE64 END ticket=$ticketId")
                    }
                }
                when (val result = repo.sendTicketMessage(ticketId, cleanMessage, image)) {
                    is NetworkResult.Success -> {
                        if (selectedImage != null) {
                            _state.value = UiState.Success(
                                sentPhotos.add(
                                    ticketId,
                                    cleanMessage,
                                    selectedImage
                                )
                            )
                        }
                        draft = ""
                        attachment = null
                        fetchMessages(showLoading = false, showError = false)
                    }

                    is NetworkResult.Failure -> {
                        if (result.error is HttpError.TicketClosed) {
                            closedTicketMessage = result.error.message
                            draft = ""
                            attachment = null
                        } else {
                            sendEvent(UiEvent.ShowSnackBar(result.error.message))
                        }
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: IllegalArgumentException) {
                sendEvent(UiEvent.ShowSnackBar(e.message ?: "Unsupported photo format."))
            } catch (e: Exception) {
                sendEvent(UiEvent.ShowSnackBar("Unable to send. Please try again."))
            } finally {
                _isSending.value = false
            }
        }
    }

    private fun startPolling() {
        screenModelScope.launch {
            fetchMessages(showLoading = true, showError = true)

            while (true) {
                delay(POLL_INTERVAL_MS)
                fetchMessages(showLoading = false, showError = false)
            }
        }
    }

    private suspend fun fetchMessages(
        showLoading: Boolean,
        showError: Boolean
    ) {
        if (showLoading && _state.value !is UiState.Success) {
            _state.value = UiState.Loading
        }

        when (val result = repo.getTicketMessages(ticketId)) {
            is NetworkResult.Success -> {
                val messages = sentPhotos.update(result.data)
                val previous = (_state.value as? UiState.Success<List<TicketMessageItem>>)?.data
                if (messages != previous) _state.value = UiState.Success(messages)
            }

            is NetworkResult.Failure -> {
                if (showError) {
                    if (_state.value !is UiState.Success) _state.value =
                        UiState.Error(result.error.message)
                    sendEvent(
                        UiEvent.ShowSnackBar(
                            message = result.error.message,
                            isError = true
                        )
                    )
                }
            }
        }
    }

    companion object {
        private const val POLL_INTERVAL_MS = 30_000L
    }
}
