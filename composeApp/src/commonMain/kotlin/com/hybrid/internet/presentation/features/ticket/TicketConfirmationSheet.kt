package com.hybrid.internet.presentation.features.ticket

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybrid.internet.data.model.response.TicketData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketConfirmationSheet(
    ticket: TicketData,
    submitting: Boolean,
    error: String?,
    onDismiss: () -> Unit,
    onSubmit: (Boolean, Int, String) -> Unit
) {
    var resolved by rememberSaveable(ticket.ticketId) { mutableStateOf<Boolean?>(null) }
    var rating by rememberSaveable(ticket.ticketId) { mutableStateOf(0) }
    var remark by rememberSaveable(ticket.ticketId) { mutableStateOf("") }
    val colors = MaterialTheme.colorScheme
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { !submitting }
    )

    ModalBottomSheet(
        onDismissRequest = { if (!submitting) onDismiss() },
        sheetState = sheetState,
        containerColor = colors.surface,
        dragHandle = { if (!submitting) BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().imePadding()
                .verticalScroll(rememberScrollState()).padding(horizontal = 24.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Issue resolved?", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(ticket.ticketId.orEmpty(), style = MaterialTheme.typography.labelSmall,
                    color = colors.onSurface.copy(alpha = 0.55f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                listOf(true to "Resolved", false to "Unresolved").forEach { (value, label) ->
                    val active = resolved == value
                    Surface(
                        onClick = { resolved = value }, enabled = !submitting,
                        modifier = Modifier.weight(1f).semantics { role = Role.RadioButton; selected = active },
                        shape = RoundedCornerShape(16.dp),
                        color = if (active) colors.primary.copy(alpha = 0.12f) else colors.surface,
                        border = BorderStroke(if (active) 2.dp else 1.dp,
                            if (active) colors.primary else colors.onSurface.copy(alpha = 0.18f))
                    ) {
                        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                            Text(label, fontWeight = FontWeight.Bold,
                                color = if (active) colors.primary else colors.onSurface)
                        }
                    }
                }
            }
            if (resolved == true) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center) {
                    (1..5).forEach { star ->
                        IconButton(onClick = { rating = star }, enabled = !submitting,
                            modifier = Modifier.weight(1f).height(48.dp).semantics { selected = rating == star }) {
                            Icon(if (star <= rating) Icons.Default.Star else Icons.Outlined.StarBorder,
                                contentDescription = "$star out of 5 stars", tint = colors.primary,
                                modifier = Modifier.size(28.dp))
                        }
                    }
                    Text(if (rating == 0) "- / 5" else "$rating / 5",
                        color = colors.primary, style = MaterialTheme.typography.labelLarge)
                }
            }
            OutlinedTextField(
                value = remark, onValueChange = { remark = it }, enabled = !submitting,
                label = { Text("Remark (optional)") },
                modifier = Modifier.fillMaxWidth(), minLines = 1, maxLines = 3,
                shape = RoundedCornerShape(16.dp)
            )
            if (error != null) {
                Text(error, color = colors.error, style = MaterialTheme.typography.bodyMedium)
            }
            Button(
                onClick = { resolved?.let { onSubmit(it, rating, remark) } },
                enabled = !submitting && resolved != null && (resolved == false || rating in 1..5),
                modifier = Modifier.fillMaxWidth().heightIn(min = 46.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (submitting) {
                    CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.width(10.dp))
                    Text("Submitting…")
                } else {
                    Text("Submit feedback")
                }
            }
        }
    }
}
