package com.business.cmpproject.presentation.features.otp

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.business.cmpproject.core.state.UiState
import com.business.cmpproject.presentation.components.AppScaffold
import com.business.cmpproject.presentation.features.dashboard.DashboardScreen

class OtpVerifyScreen(
    private val mobile: String
) : Screen {

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<OtpScreenModel>()
        val navigator = LocalNavigator.current!!

        AppScaffold(events = viewModel.events) {

            OtpVerifyContent(
                mobileMasked = mobile.replaceRange(4, 8, "XXXX"),
                onBack = { navigator.pop() },

                // ✅ SEND OTP (AUTO + RESEND)
                onSendOtp = {
                    viewModel.sendOtp(mobile)
                },

                // ✅ VERIFY OTP
                onVerify = { otp ->
                    viewModel.verifyOtp(mobile, otp)
                },
                viewModel = viewModel,
                onOtpVerifySuccess = {
                    navigator.replace(DashboardScreen())
                },
            )

//            if (viewModel.state.value is UiState.Success) {
//                navigator.replace(DashboardScreen())
//            }
        }
    }
}
