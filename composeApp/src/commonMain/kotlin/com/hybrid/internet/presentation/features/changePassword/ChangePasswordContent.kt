package com.hybrid.internet.presentation.features.changePassword

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybrid.internet.core.validation.Validator
import com.hybrid.internet.presentation.components.AppButton
import com.hybrid.internet.presentation.components.PasswordField
import com.hybrid.internet.presentation.components.ScreenContainer

@Composable
fun ChangePasswordContent(
    onPasswordChanged: () -> Unit
) {
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    ScreenContainer {

        Text(
            text = "Change Password",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(32.dp))

        PasswordField(password, { password = it }, "New Password")
        Spacer(Modifier.height(16.dp))
        PasswordField(confirm, { confirm = it }, "Confirm Password")

        Spacer(Modifier.height(24.dp))

        AppButton(
            text = "Update Password",
            loading = false
        ) {
            error =
                Validator.password(password)
                    ?: if (password != confirm) "Passwords do not match" else null

            if (error == null) {
                onPasswordChanged()
            }
        }

        error?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}
