package com.hybrid.internet.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppButton(
    text: String,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    enabled: Boolean = true,
    fullWidth: Boolean = true,
    alignment: ButtonAlignment = ButtonAlignment.CENTER,
    onClick: () -> Unit
) {
    val isEnabled = enabled && !loading

    // Wrapper to control alignment when not full width
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = when {
            fullWidth -> Alignment.Center
            alignment == ButtonAlignment.START -> Alignment.CenterStart
            alignment == ButtonAlignment.END -> Alignment.CenterEnd
            else -> Alignment.Center
        }
    ) {

        Button(
            onClick = onClick,
            enabled = isEnabled,
            modifier = Modifier
                .then(
                    if (fullWidth) Modifier.fillMaxWidth()
                    else Modifier.wrapContentWidth()
                )
                .height(52.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor =
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContentColor =
                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
            )
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
