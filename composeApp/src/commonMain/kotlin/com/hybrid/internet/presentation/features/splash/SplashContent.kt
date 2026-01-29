package com.hybrid.internet.presentation.features.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hybrid.internet.composeapp.generated.resources.Res
import com.hybrid.internet.composeapp.generated.resources.app_logo
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

@Composable
fun SplashContent(
    viewModel: SplashScreenModel,
    onResult: (Boolean) -> Unit
) {
    LaunchedEffect(Unit) {
        delay(1800) // subtle, not too long
        onResult(viewModel.isLoggedIn())
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔹 App Logo
            Image(
                painter = painterResource(Res.drawable.app_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(70.dp).padding(5.dp)
            )

            // 🔹 App Name / Logo Placeholder
            Text(
                text = "HYBRID Internet",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 🔹 Tagline (optional but premium)
            Text(
                text = "\uD83D\uDD39 Kal ko kareeb laye",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 🔹 Minimal loading indicator
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                strokeWidth = 3.dp
            )
        }
    }
}
