package com.business.cmpproject.presentation.features.changePassword

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator

class ChangePasswordScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current!!

        ChangePasswordContent(
            onPasswordChanged = {
                // After password change, go back to login
                navigator.pop()
            }
        )
    }
}