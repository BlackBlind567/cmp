package com.hybrid.internet.presentation.features.ticketMessages

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.material3.SnackbarHostState
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hybrid.internet.core.state.UiEvent
import org.koin.core.parameter.parametersOf

data class TicketMessagesScreen(
    val ticketId: String,
    val displayTicketId: String = ticketId
) : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<TicketMessagesScreenModel> {
            parametersOf(ticketId)
        }
        val uiState by screenModel.state.collectAsState()
        val isSending by screenModel.isSending.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val isDark = isSystemInDarkTheme()

        val snackbarHostState = remember { SnackbarHostState() }
        LaunchedEffect(screenModel) {
            screenModel.events.collect { event ->
                if (event is UiEvent.ShowSnackBar) {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }

        TicketMessagesContent(
            snackbarHostState = snackbarHostState,
            uiState = uiState,
            ticketId = displayTicketId,
            isSending = isSending,
            closedTicketMessage = screenModel.closedTicketMessage,
            isDark = isDark,
            onBack = { navigator.pop() },
            onRetry = { screenModel.loadMessages() },
            messageText = screenModel.draft,
            attachment = screenModel.attachment,
            onMessageChange = screenModel::updateDraft,
            onAttachmentChange = screenModel::attachImage,
            onSendMessage = screenModel::sendMessage
        )
    }
}
