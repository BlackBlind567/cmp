package com.hybrid.internet.presentation.features.web

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.coroutines.delay
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKWebView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PlatformWebView(
    url: String,
    modifier: Modifier,
    onLoadingChange: (Boolean) -> Unit
) {
    LaunchedEffect(url) {
        onLoadingChange(true)
        delay(1200)
        onLoadingChange(false)
    }

    UIKitView(
        modifier = modifier,
        factory = {
            WKWebView().apply {
                NSURL.URLWithString(url)?.let { requestUrl ->
                    loadRequest(NSURLRequest.requestWithURL(requestUrl))
                }
            }
        },
        update = { webView ->
            NSURL.URLWithString(url)?.let { requestUrl ->
                webView.loadRequest(NSURLRequest.requestWithURL(requestUrl))
            }
        }
    )
}
