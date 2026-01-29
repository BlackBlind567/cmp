package com.hybrid.internet.presentation.features.changePassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybrid.internet.core.base.BaseScreenModel
import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.core.state.UiEvent
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.core.validation.Validator
import com.hybrid.internet.data.model.request.ChangePasswordRequest
import com.hybrid.internet.domain.repository.changePassword.ChangePasswordRepository
import com.hybrid.internet.presentation.components.AppButton
import com.hybrid.internet.presentation.components.AppTextField
import com.hybrid.internet.presentation.components.InputType
import com.hybrid.internet.presentation.components.PasswordField
import com.hybrid.internet.presentation.components.ScreenContainer
import com.hybrid.internet.presentation.components.StandardTopAppBar
import com.hybrid.internet.presentation.theme.CreamBackground
import com.hybrid.internet.presentation.theme.DarkBackground
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordContent(
    isLoading: Boolean,
    onSubmit: (String, String, String) -> Unit,
    onBack: () -> Unit = {},
    isDark: Boolean,
) {
    var current by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    var currentError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmError by remember { mutableStateOf<String?>(null) }
    Scaffold(
        containerColor = if (isDark) DarkBackground else CreamBackground,
        topBar = {
            StandardTopAppBar(
                title = "Change Password",
                subtitle =  "Update your password",
                showBack = true,
                onBack = onBack
            )
        }
    ) { padding ->
    Column(modifier = Modifier.padding(padding).padding(horizontal = 16.dp)) {



        Spacer(Modifier.height(12.dp))

        AppTextField(
            value = current,
            onChange = {
                current = it
                currentError = null
            },
            label = "Current Password",
            error = currentError
        )

        Spacer(Modifier.height(16.dp))

        AppTextField(
            value = password,
            onChange = {
                password = it
                passwordError = null
            },
            label = "New Password",
            inputType = InputType.PASSWORD,
            error = passwordError
        )

        Spacer(Modifier.height(16.dp))

        AppTextField(
            value = confirm,
            onChange = {
                confirm = it
                confirmError = null
            },
            label = "Confirm Password",
            inputType = InputType.PASSWORD,
            error = confirmError
        )

        Spacer(Modifier.height(24.dp))

        AppButton(
            text = "Update Password",
            loading = isLoading
        ) {

            // 🔥 STEP‑BY‑STEP VALIDATION
            currentError =
                if (current.isBlank()) "Current password is required" else null

            passwordError = Validator.strongPassword(password)

            confirmError = Validator.confirmPassword(password, confirm)

            if (currentError == null &&
                passwordError == null &&
                confirmError == null
            ) {
                onSubmit(current, password, confirm)
            }
        }
    }}
}

