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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hybrid.internet.presentation.components.AppScaffold
import com.hybrid.internet.presentation.components.StandardTopAppBar
import com.hybrid.internet.presentation.features.login.LoginScreen
import com.hybrid.internet.presentation.theme.CreamBackground
import com.hybrid.internet.presentation.theme.DarkBackground
import com.hybrid.internet.presentation.theme.GreenPrimary
import com.hybrid.internet.presentation.theme.GreenSecondary
import com.hybrid.internet.presentation.theme.PinkPrimary
import com.hybrid.internet.presentation.theme.PinkSecondary

class ProfileScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val rootNavigator = navigator.parent ?: navigator
        val screenModel = getScreenModel<ProfileScreenModel>()
        val user = screenModel.userData.collectAsState().value
        val isDark = isSystemInDarkTheme()

        AppScaffold(events = screenModel.events) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isDark) DarkBackground else CreamBackground)
            ) {
                // Custom TopBar (Jo pehle banaya tha)
                StandardTopAppBar(
                    title = "My Profile",
                    isTitleCenter = false,
                    showBack = false
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // --- Profile Header (Avatar + Name) ---
                    Surface(
                        modifier = Modifier.size(90.dp),
                        shape = CircleShape,
                        color = (if (isDark) PinkPrimary else GreenPrimary).copy(alpha = 0.1f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = user?.customer_name?.take(1)?.uppercase() ?: "U",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Black,
                                color = if (isDark) PinkPrimary else GreenPrimary
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = user?.customer_name ?: "User Name",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = user?.company_name ?: "Company Name",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isDark) PinkSecondary else GreenSecondary
                    )

                    Spacer(Modifier.height(32.dp))

                    // --- User Info Section ---
                    ProfileInfoCard(user, isDark)

                    Spacer(Modifier.height(24.dp))

                    // --- Action List ---
                    Text(
                        "Account Settings",
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )

                    ActionItem(
                        icon = Icons.Default.LockReset,
                        title = "Change Password",
                        isDark = isDark,
                        onClick = { /* navigator.push(ChangePasswordScreen()) */ }
                    )

                    ActionItem(
                        icon = Icons.AutoMirrored.Filled.Logout,
                        title = "Logout",
                        isDark = isDark,
                        isCritical = true,
                        onClick = { screenModel.logout { rootNavigator.replaceAll(LoginScreen()) } }
                    )
                }
            }
        }
    }
}