package com.hybrid.internet.presentation.features.ticketMessages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybrid.internet.core.image.rememberImagePicker
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.data.model.response.TicketMessageItem
import com.hybrid.internet.presentation.components.FullScreenError
import com.hybrid.internet.presentation.components.StandardTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketMessagesContent(
    snackbarHostState: SnackbarHostState,
    uiState: UiState<List<TicketMessageItem>>,
    ticketId: String,
    isSending: Boolean,
    closedTicketMessage: String?,
    isDark: Boolean,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    messageText: String,
    attachment: ByteArray?,
    onMessageChange: (String) -> Unit,
    onAttachmentChange: (ByteArray?) -> Unit,
    onSendMessage: () -> Unit
) {
    val picker = rememberImagePicker { bytes -> if (bytes != null) onAttachmentChange(bytes) }
    val accent = MaterialTheme.colorScheme.primary
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    LaunchedEffect(closedTicketMessage) {
        if (closedTicketMessage != null) {
            focusManager.clearFocus(force = true)
            keyboard?.hide()
        }
    }
    Scaffold(
        // This scaffold is the sole owner of keyboard insets for the chat.
        modifier = Modifier.fillMaxSize().imePadding(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            StandardTopAppBar(title = "Support chat", subtitle = ticketId,
                showBack = true, onBack = onBack, isTitleCenter = false)
        },
        bottomBar = {
            if (closedTicketMessage != null) {
                Surface(color = MaterialTheme.colorScheme.surface) {
                    Text(
                        text = closedTicketMessage,
                        modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(16.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            } else {
            Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 4.dp) {
                Column(Modifier.fillMaxWidth().navigationBarsPadding().padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (attachment != null) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            ChatAttachment(bytes = attachment, modifier = Modifier.size(64.dp))
                            Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                                Text("Photo attached", style = MaterialTheme.typography.labelLarge)
                                Text("Ready to send", style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f))
                            }
                            IconButton(onClick = { onAttachmentChange(null) }, enabled = !isSending) {
                                Icon(Icons.Default.Close, "Remove attachment")
                            }
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = picker::pickImage, enabled = !isSending) {
                            Icon(Icons.Default.AttachFile, "Attach photo", tint = accent)
                        }
                        OutlinedTextField(value = messageText, onValueChange = onMessageChange,
                            modifier = Modifier.weight(1f), enabled = !isSending,
                            placeholder = { Text("Message…") }, maxLines = 4,
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)))
                        Spacer(Modifier.width(8.dp))
                        FilledIconButton(onClick = onSendMessage,
                            enabled = !isSending && (messageText.isNotBlank() || attachment != null),
                            modifier = Modifier.size(48.dp)) {
                            if (isSending) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                            else Icon(Icons.Default.Send, "Send message")
                        }
                    }
                }
            }
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Box(Modifier.padding(padding).consumeWindowInsets(padding).fillMaxSize()) {
            when (uiState) {
                UiState.Idle, UiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is UiState.Error -> FullScreenError(message = uiState.message, onRetry = onRetry)
                is UiState.Success -> {
                    if (uiState.data.isEmpty()) {
                        Column(Modifier.align(Alignment.Center).padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Icon(Icons.Default.Forum, null, tint = accent, modifier = Modifier.size(36.dp))
                            Text("How can we help?", style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold)
                            Text("Send a message or share a photo.", style = MaterialTheme.typography.bodyMedium)
                        }
                    } else ChatMessagesList(uiState.data, isDark)
                }
                else -> Unit
            }
        }
    }
}

@Composable
private fun ChatMessagesList(messages: List<TicketMessageItem>, isDark: Boolean) {
    val listState = rememberLazyListState()
    var previousCount by remember { mutableStateOf(0) }
    LaunchedEffect(messages.size) {
        val wasAtBottom = previousCount == 0 ||
            (listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) >= previousCount - 1
        if (messages.isNotEmpty() && wasAtBottom) {
            listState.scrollToItem(messages.lastIndex)
        }
        previousCount = messages.size
    }
    LazyColumn(state = listState, modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        itemsIndexed(messages, key = { index, item -> item.localKey ?: item.id ?: "message-$index" }) { _, item ->
            ChatBubble(item, isDark)
        }
    }
}

@Composable
private fun ChatBubble(item: TicketMessageItem, isDark: Boolean) {
    val own = item.senderType.equals("customer", true) || item.senderType.equals("user", true)
    val colors = MaterialTheme.colorScheme
    Row(Modifier.fillMaxWidth(), horizontalArrangement = if (own) Arrangement.End else Arrangement.Start) {
        Column(
            modifier = Modifier.widthIn(max = 300.dp),
            horizontalAlignment = if (own) Alignment.End else Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(18.dp, 18.dp, if (own) 4.dp else 18.dp, if (own) 18.dp else 4.dp),
                color = if (own) colors.primary else colors.surface
            ) {
                Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    if (!own) Text(item.senderName ?: item.companyName ?: "Support",
                        style = MaterialTheme.typography.labelSmall, color = colors.primary.copy(alpha = 0.8f))
                    if (item.localImage != null || !item.image.isNullOrBlank()) {
                        ChatAttachment(source = item.image, bytes = item.localImage,
                            modifier = Modifier.size(72.dp))
                    }
                    if (!item.message.isNullOrBlank()) {
                        Text(item.message.orEmpty(), style = MaterialTheme.typography.bodyMedium,
                            color = if (own) colors.onPrimary else colors.onSurface)
                    }
                }
            }
            item.createdAt?.let { timestamp ->
                Text(chatTime(timestamp), modifier = Modifier.padding(horizontal = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.onBackground.copy(alpha = 0.5f))
            }
        }
    }
}

private fun chatTime(timestamp: String): String = when {
    timestamp.contains(" AM") || timestamp.contains(" PM") -> timestamp.substringAfter(' ')
    timestamp.contains('T') -> timestamp.substringAfter('T').take(5)
    else -> timestamp
}
