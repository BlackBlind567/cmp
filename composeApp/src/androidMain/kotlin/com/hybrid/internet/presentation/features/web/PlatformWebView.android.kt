package com.hybrid.internet.presentation.features.web

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun PlatformWebView(
    url: String,
    modifier: Modifier,
    onLoadingChange: (Boolean) -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                setBackgroundColor(android.graphics.Color.TRANSPARENT)
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        onLoadingChange(true)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        onLoadingChange(false)
                    }
                }
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                loadUrl(url)
            }
        },
        update = { webView ->
            if (webView.url != url) {
                onLoadingChange(true)
                webView.loadUrl(url)
            }
        }
    )
}
