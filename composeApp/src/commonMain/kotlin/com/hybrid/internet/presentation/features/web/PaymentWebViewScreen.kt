package com.hybrid.internet.presentation.features.web

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hybrid.internet.presentation.components.AppScaffold
import com.hybrid.internet.presentation.components.StandardTopAppBar
import com.hybrid.internet.presentation.theme.CreamBackground
import com.hybrid.internet.presentation.theme.CreamSurface
import com.hybrid.internet.presentation.theme.DarkBackground
import com.hybrid.internet.presentation.theme.DarkSurface
import com.hybrid.internet.presentation.theme.GreenPrimary
import com.hybrid.internet.presentation.theme.PinkPrimary
import kotlinx.coroutines.flow.emptyFlow

data class PaymentWebViewScreen(
    val url: String,
    val title: String = "Payment"
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val isDark = isSystemInDarkTheme()
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        val accentColor = if (isDark) PinkPrimary else GreenPrimary
        var isLoading by remember(url) { mutableStateOf(true) }

        AppScaffold(events = emptyFlow()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isDark) DarkBackground else CreamBackground)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                StandardTopAppBar(
                    title = title,
                    subtitle = "Secure page",
                    showBack = true,
                    onBack = { navigator.pop() },
                    scrollBehavior = scrollBehavior,
                    isTitleCenter = false
                )

                if (isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = accentColor,
                        trackColor = accentColor.copy(alpha = 0.14f)
                    )
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    PlatformWebView(
                        url = url,
                        modifier = Modifier.fillMaxSize(),
                        onLoadingChange = { isLoading = it }
                    )

                    if (isLoading) {
                        WebViewLoadingOverlay(
                            url = url,
                            isDark = isDark,
                            accentColor = accentColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
expect fun PlatformWebView(
    url: String,
    modifier: Modifier = Modifier,
    onLoadingChange: (Boolean) -> Unit = {}
)

@Composable
private fun WebViewLoadingOverlay(
    url: String,
    isDark: Boolean,
    accentColor: Color
) {
    val surfaceColor = if (isDark) DarkSurface else CreamSurface
    val textColor = if (isDark) Color.White else Color(0xFF1A1A1A)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background((if (isDark) DarkBackground else CreamBackground).copy(alpha = 0.92f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(24.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = surfaceColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = accentColor,
                    strokeWidth = 3.dp
                )
                Text(
                    text = "Opening secure page",
                    modifier = Modifier.padding(top = 18.dp),
                    color = textColor,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = url,
                    modifier = Modifier.padding(top = 6.dp),
                    color = textColor.copy(alpha = 0.62f),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
