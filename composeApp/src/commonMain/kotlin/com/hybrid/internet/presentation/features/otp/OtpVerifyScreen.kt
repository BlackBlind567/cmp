package com.hybrid.internet.presentation.features.otp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.presentation.components.AppScaffold
import com.hybrid.internet.presentation.features.dashboard.DashboardScreen

class OtpVerifyScreen(
    private val mobile: String
) : Screen {

    @Composable
    override fun Content() {

        val viewModel = getScreenModel<OtpScreenModel>()
        val navigator = LocalNavigator.current!!
        val state by viewModel.state.collectAsState()

        // ✅ Navigation handled safely
        LaunchedEffect(state) {
            if (state is UiState.Success) {
                navigator.replace(DashboardScreen())
            }
        }

        AppScaffold(events = viewModel.events) {

            OtpVerifyContent(
                mobileMasked = mobile.replaceRange(4, 8, "XXXX"),
                onBack = { navigator.pop() },

                onSendOtp = {
                    viewModel.sendOtp(mobile)
                },

                onVerify = { otp ->
                    viewModel.verifyOtp(mobile, otp)
                },

                viewModel = viewModel,
                onOtpVerifySuccess = {}
            )
        }
    }
}