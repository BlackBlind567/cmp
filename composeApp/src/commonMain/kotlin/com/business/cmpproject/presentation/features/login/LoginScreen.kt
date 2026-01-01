package com.business.cmpproject.presentation.features.login


import androidx.compose.material3.*
import androidx.compose.runtime.*
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.business.cmpproject.presentation.components.AppScaffold
import com.business.cmpproject.presentation.features.changePassword.ChangePasswordScreen
import com.business.cmpproject.presentation.features.dashboard.DashboardScreen
import com.business.cmpproject.presentation.features.otp.OtpVerifyScreen



class LoginScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<LoginScreenModel>()
        val navigator = LocalNavigator.current!!

        AppScaffold(events = viewModel.events) {
            LoginContent(
                viewModel = viewModel,

                onEmailLoginSuccess = {
                    navigator.replace(DashboardScreen())
                },

                // ✅ MOBILE LOGIN → OTP SCREEN
                onMobileLogin = { mobile ->
                    navigator.push(
                        OtpVerifyScreen(
                            mobile = mobile
                        )
                    )
                },

                onForgotPassword = {
                    navigator.push(ChangePasswordScreen())
                }
            )
        }
    }
}
