package com.hybrid.internet.presentation.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hybrid.internet.core.state.UiEvent
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.presentation.components.AppScaffold
import com.hybrid.internet.presentation.components.StandardTopAppBar
import com.hybrid.internet.presentation.features.changePassword.ChangePasswordScreen
import com.hybrid.internet.presentation.features.login.LoginScreen
import com.hybrid.internet.presentation.theme.CreamBackground
import com.hybrid.internet.presentation.theme.DarkBackground
import com.hybrid.internet.presentation.theme.GreenPrimary
import com.hybrid.internet.presentation.theme.GreenSecondary
import com.hybrid.internet.presentation.theme.PinkPrimary
import com.hybrid.internet.presentation.theme.PinkSecondary

class ProfileScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val rootNavigator = navigator.parent ?: navigator
        val screenModel = getScreenModel<ProfileScreenModel>()

        val user by screenModel.userData.collectAsState()
        val state by screenModel.state.collectAsState()
        val isDark = isSystemInDarkTheme()

        // 🔥 Side-effects ONLY here
        LaunchedEffect(Unit) {
            screenModel.events.collect { event ->
                when (event) {
                    UiEvent.NavigateToLogin -> {
                        rootNavigator.replaceAll(LoginScreen())
                    }
                    else -> Unit
                }
            }
        }

        AppScaffold(events = screenModel.events) {
            ProfileContent(
                user = user,
                isDark = isDark,
                isLoggingOut = state is UiState.Loading,
                onChangePassword = {
                    rootNavigator.push(ChangePasswordScreen())
                },
                onLogout = {
                    screenModel.logout()
                }
            )
        }
    }
}
