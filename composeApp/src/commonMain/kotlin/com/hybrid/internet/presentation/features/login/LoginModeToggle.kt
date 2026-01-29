package com.hybrid.internet.presentation.features.login

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybrid.internet.presentation.components.LoginMode

@Composable
fun LoginModeToggle(
    selected: LoginMode,
    onSelect: (LoginMode) -> Unit
) {
    Row {
        LoginModeChip(
            text = "Email",
            selected = selected == LoginMode.EMAIL,
            onClick = { onSelect(LoginMode.EMAIL) }
        )

        Spacer(Modifier.width(12.dp))

        LoginModeChip(
            text = "Mobile",
            selected = selected == LoginMode.MOBILE,
            onClick = { onSelect(LoginMode.MOBILE) }
        )
    }
}
