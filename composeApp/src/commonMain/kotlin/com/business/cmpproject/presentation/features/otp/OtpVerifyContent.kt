package com.business.cmpproject.presentation.features.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.business.cmpproject.core.state.UiState
import com.business.cmpproject.presentation.components.AppButton
import com.business.cmpproject.presentation.components.OtpInput
import com.business.cmpproject.presentation.components.ScreenContainer
import com.business.cmpproject.presentation.features.login.LoginScreenModel
import kotlinx.coroutines.delay

@Composable
fun OtpVerifyContent(
    viewModel: OtpScreenModel,
    mobileMasked: String,
    onBack: () -> Unit,
    onSendOtp: () -> Unit,
    onVerify: (String) -> Unit,
    onOtpVerifySuccess: () -> Unit
) {
    var otp by remember { mutableStateOf("") }
    var timeLeft by remember { mutableStateOf(120) }
    val state by viewModel.state.collectAsState()
    // Send OTP on screen open
    LaunchedEffect(Unit) {
        onSendOtp()
    }

    // Countdown timer
    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
    }

    ScreenContainer {

        /* ---------- HEADER ---------- */
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Verify OTP",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Code sent to $mobileMasked",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(Modifier.height(40.dp))

        /* ---------- OTP INPUT ---------- */
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            OtpInput(
                otp = otp,
                onOtpChange = { if (it.length <= 6) otp = it },
                modifier = Modifier.fillMaxWidth(0.85f) // 🔑 responsive width
            )
        }

        Spacer(Modifier.height(32.dp))

        /* ---------- RESEND TIMER ---------- */
        Text(
            text = if (timeLeft > 0)
                "Resend in ${formatTime(timeLeft)}"
            else
                "Didn’t receive OTP?",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        if (timeLeft == 0) {
            TextButton(
                onClick = {
                    timeLeft = 120
                    otp = ""
                    onSendOtp()
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Resend OTP")
            }
        }

        Spacer(Modifier.height(32.dp))

        /* ---------- VERIFY BUTTON ---------- */
        AppButton(
            text = "Verify OTP",
            enabled = otp.length == 6
        ) {
            onVerify(otp)
        }

        if (state is UiState.Success) {
           onOtpVerifySuccess()
        }
    }
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60

    val minStr = minutes.toString().padStart(2, '0')
    val secStr = secs.toString().padStart(2, '0')

    return "$minStr:$secStr"
}
