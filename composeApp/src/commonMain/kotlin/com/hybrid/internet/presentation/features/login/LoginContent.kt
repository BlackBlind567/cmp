package com.hybrid.internet.presentation.features.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.core.validation.Validator
import com.hybrid.internet.presentation.components.AppButton
import com.hybrid.internet.presentation.components.AppTextField
import com.hybrid.internet.presentation.components.InputType
import com.hybrid.internet.presentation.components.LoginMode
import com.hybrid.internet.presentation.components.ScreenContainer
import kotlinx.coroutines.launch

@Composable
fun LoginContent(
    viewModel: LoginScreenModel,
    onEmailLoginSuccess: () -> Unit,
    onMobileLogin: (String) -> Unit,

    onForgotPassword: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()

    var loginMode by remember { mutableStateOf(LoginMode.EMAIL) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var mobileError by remember { mutableStateOf<String?>(null) }

    ScreenContainer {

        /* ---------- HEADER ---------- */
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Choose a login method to continue",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(Modifier.height(28.dp))

        /* ---------- LOGIN METHOD SECTION ---------- */
        LoginMethodSelector(
            selected = loginMode,
            onSelect = { loginMode = it }
        )

        Spacer(Modifier.height(28.dp))

        /* ---------- FORM SECTION ---------- */
        when (loginMode) {

            LoginMode.EMAIL -> {
                AppTextField(
                    value = email,
                    onChange = {
                        email = it
                        emailError = null
                    },
                    label = "Email",
                    inputType = InputType.EMAIL,
                    error = emailError
                )

                Spacer(Modifier.height(16.dp))

                AppTextField(
                    value = password,
                    onChange = {
                        password = it
                        passwordError = null
                    },
                    label = "Password",
                    inputType = InputType.PASSWORD,
                    error = passwordError
                )

                Spacer(Modifier.height(8.dp))

                /* ---------- FORGOT PASSWORD ---------- */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onForgotPassword) {
                        Text("Forgot password?")
                    }
                }

                Spacer(Modifier.height(16.dp))

                AppButton(
                    text = "Login",
                    loading = state is UiState.Loading,
                    enabled = email.isNotBlank() && password.isNotBlank()
                ) {
                    emailError = Validator.email(email)
                    passwordError = Validator.password(password)

                    if (emailError == null && passwordError == null) {
                        scope.launch {
                            viewModel.login(email, password)
                        }
                    }
                }
            }

            LoginMode.MOBILE -> {
                AppTextField(
                    value = mobile,
                    onChange = { input ->
                        if (input.length <= 10 && input.all { it.isDigit() }) {
                            mobile = input
                        }
                    },
                    label = "Mobile Number",
                    inputType = InputType.NUMBER,
                    error = mobileError
                )

                Spacer(Modifier.height(24.dp))

                AppButton(
                    text = "Send OTP",
                    enabled = mobile.length == 10
                ) {
                    mobileError = Validator.mobile(mobile)
                    if (mobileError == null) {
                        onMobileLogin(mobile)
                    }
                }
            }
        }


        /* ---------- SUCCESS ---------- */
        if (state is UiState.Success) {
            onEmailLoginSuccess()
        }
    }
}


