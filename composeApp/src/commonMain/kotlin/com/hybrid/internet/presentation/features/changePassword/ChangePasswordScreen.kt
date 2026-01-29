package com.hybrid.internet.presentation.features.changePassword

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hybrid.internet.core.state.UiEvent
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.presentation.components.AppScaffold

class ChangePasswordScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<ChangePasswordScreenModel>()
        val state by screenModel.state.collectAsState()
        val isDark = isSystemInDarkTheme()

        // ✅ Handle one-time UI events here
        LaunchedEffect(Unit) {
            screenModel.events.collect { event ->
                when (event) {
                    UiEvent.NavigateBack -> navigator.pop()
                    else -> Unit
                }
            }
        }

        AppScaffold(events = screenModel.events) {

            ChangePasswordContent(
                isLoading = state is UiState.Loading,
                onSubmit = { current, password, confirm ->
                    screenModel.changePassword(
                        current,
                        password,
                        confirm
                    )
                },
                onBack = {navigator.pop()},
                isDark = isDark
            )
        }
    }
}
