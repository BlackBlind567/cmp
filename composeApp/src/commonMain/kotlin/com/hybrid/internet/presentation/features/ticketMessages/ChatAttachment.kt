package com.hybrid.internet.presentation.features.ticketMessages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hybrid.internet.core.config.AppConfig
import com.hybrid.internet.core.image.decodeImage
import com.hybrid.internet.core.network.provideHttpEngine
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.plugins.HttpTimeout
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalEncodingApi::class)
@Composable
fun ChatAttachment(source: String? = null, bytes: ByteArray? = null, modifier: Modifier = Modifier) {
    val imageKey = source?.substringBefore('?')
    var bitmap by remember(imageKey, bytes) { mutableStateOf<ImageBitmap?>(null) }
    var failed by remember(imageKey, bytes) { mutableStateOf(false) }
    var expanded by remember(imageKey, bytes) { mutableStateOf(false) }
    var attempt by remember(imageKey, bytes) { mutableStateOf(0) }
    val currentSource by rememberUpdatedState(source)
    LaunchedEffect(imageKey, bytes, attempt) {
        val source = currentSource
        failed = false
        try {
            bitmap = withContext(Dispatchers.IO) {
                val data = bytes ?: when {
                    source.isNullOrBlank() -> error("Missing image")
                    source.startsWith("data:image/") -> Base64.decode(source.substringAfter(','))
                    source.startsWith("/9j/") || source.startsWith("iVBOR") ||
                        source.startsWith("R0lGOD") || source.startsWith("UklGR") -> Base64.decode(source)
                    source.startsWith("https://") || source.startsWith("http://") ||
                        source.startsWith("/") || source.startsWith("storage/") || source.startsWith("uploads/") -> {
                        val url = if (source.startsWith("http")) source
                            else AppConfig.BASE_URL.trimEnd('/') + "/" + source.trimStart('/')
                        // Attachment URLs may point to a CDN; do not forward the API bearer token.
                        val client = HttpClient(provideHttpEngine()) {
                            expectSuccess = true
                            install(HttpTimeout) { requestTimeoutMillis = 20_000 }
                        }
                        try { client.get(url).body<ByteArray>() } finally { client.close() }
                    }
                    else -> Base64.decode(source)
                }
                decodeImage(data) ?: error("Unsupported image")
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            failed = true
        }
    }
    Surface(modifier = modifier.clip(RoundedCornerShape(12.dp)),
        color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(12.dp)) {
        Box(contentAlignment = Alignment.Center) {
            val image = bitmap
            when {
                image != null -> Image(image, "Photo attachment. Tap to expand",
                    modifier = Modifier.fillMaxSize().clickable { expanded = true }, contentScale = ContentScale.Crop)
                failed -> TextButton(onClick = { attempt++ }) { Text("Retry photo", style = MaterialTheme.typography.labelSmall) }
                else -> CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
            }
        }
    }
    if (expanded) {
        Dialog(onDismissRequest = { expanded = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)) {
            Box(Modifier.fillMaxSize().background(Color.Black)) {
                bitmap?.let {
                    Image(it, "Photo attachment", Modifier.fillMaxSize().safeDrawingPadding(),
                        contentScale = ContentScale.Fit)
                }
                IconButton(onClick = { expanded = false },
                    modifier = Modifier.align(Alignment.TopEnd).safeDrawingPadding().padding(8.dp)) {
                    Icon(Icons.Default.Close, "Close photo", tint = Color.White)
                }
            }
        }
    }
}
