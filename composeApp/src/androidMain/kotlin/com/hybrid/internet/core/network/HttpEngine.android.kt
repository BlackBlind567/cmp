package com.hybrid.internet.core.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

actual fun provideHttpEngine(): HttpClientEngine {
    return OkHttp.create()
}