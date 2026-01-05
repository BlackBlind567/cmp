package com.business.cmpproject.core.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

actual fun provideHttpEngine(): HttpClientEngine {
    return Darwin.create()
}