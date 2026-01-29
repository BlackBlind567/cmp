package com.hybrid.internet.presentation.features.plans

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hybrid.internet.data.model.response.PlanResponse
import com.hybrid.internet.presentation.theme.CreamSurface
import com.hybrid.internet.presentation.theme.DarkSurface
import com.hybrid.internet.presentation.theme.DarkTextPrimary
import com.hybrid.internet.presentation.theme.GreenPrimary
import com.hybrid.internet.presentation.theme.LightTextPrimary
import com.hybrid.internet.presentation.theme.PinkPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagePlanSheet(
    plan: PlanResponse,
    isDark: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (newQty: Int?, reason: String, isTerminate: Boolean) -> Unit
) {
    var newQtyStr by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var isTerminateChecked by remember { mutableStateOf(false) }

    val currentQty = plan.qty ?: 0
    val newQty = newQtyStr.toIntOrNull()

    // Auto-selection Logic for UI labels
    val isUpgrade = newQty != null && newQty > currentQty
    val isDowngrade = newQty != null && newQty < currentQty
    val isValidChange = (isUpgrade || isDowngrade) && !isTerminateChecked

    val accentColor = when {
        isTerminateChecked -> Color.Red
        isUpgrade -> if (isDark) PinkPrimary else GreenPrimary
        isDowngrade -> Color(0xFFF57C00) // Orange for Downgrade
        else -> Color.Gray
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = if (isDark) DarkSurface else CreamSurface,
        dragHandle = { BottomSheetDefaults.DragHandle(color = accentColor.copy(alpha = 0.5f)) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Manage ${plan.item}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = if (isDark) DarkTextPrimary else LightTextPrimary
            )

            Spacer(Modifier.height(24.dp))

            // --- Termination Toggle ---
            Surface(
                onClick = { isTerminateChecked = !isTerminateChecked },
                shape = RoundedCornerShape(16.dp),
                color = if (isTerminateChecked) Color.Red.copy(alpha = 0.1f) else Color.Transparent,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isTerminateChecked) Color.Red else Color.Gray.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Terminate Plan", fontWeight = FontWeight.Bold, color = if(isTerminateChecked) Color.Red else Color.Gray)
                        Text("Close this connection permanently", style = MaterialTheme.typography.labelSmall)
                    }
                    Checkbox(
                        checked = isTerminateChecked,
                        onCheckedChange = { isTerminateChecked = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color.Red)
                    )
                }
            }

            if (!isTerminateChecked) {
                Spacer(Modifier.height(16.dp))
                // --- Quantity Input ---
                OutlinedTextField(
                    value = newQtyStr,
                    onValueChange = { if (it.length <= 6) newQtyStr = it.filter { c -> c.isDigit() } },
                    label = { Text("New Quantity (${plan.itemUnit})") },
                    placeholder = { Text("Current: $currentQty") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )

                if (isValidChange) {
                    Text(
                        text = "This action is an ${if (isUpgrade) "UPGRADE" else "DOWNGRADE"}",
                        color = accentColor,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // --- Reason Input ---
            OutlinedTextField(
                value = reason,
                onValueChange = { reason = it },
                label = { Text("Reason for request") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(32.dp))

            // --- Confirm Button ---
            Button(
                onClick = { onConfirm(newQty, reason, isTerminateChecked) },
                enabled = (isValidChange || isTerminateChecked) && reason.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if (isTerminateChecked) "Confirm Termination" else "Submit Request",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}