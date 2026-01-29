package com.hybrid.internet.presentation.features.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybrid.internet.presentation.components.LoginMode

@Composable
fun LoginMethodSelector(
    selected: LoginMode,
    onSelect: (LoginMode) -> Unit
) {
    Column {

        Text(
            text = "Login using",
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            LoginMethodCard(
                modifier = Modifier.weight(1f), // ✅ CORRECT PLACE
                title = "Email",
                subtitle = "Email & Password",
                selected = selected == LoginMode.EMAIL,
                onClick = { onSelect(LoginMode.EMAIL) }
            )

            LoginMethodCard(
                modifier = Modifier.weight(1f), // ✅ CORRECT PLACE
                title = "Mobile",
                subtitle = "OTP Verification",
                selected = selected == LoginMode.MOBILE,
                onClick = { onSelect(LoginMode.MOBILE) }
            )
        }
    }
}
