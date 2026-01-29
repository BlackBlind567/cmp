package com.hybrid.internet.core.network

import io.ktor.client.engine.HttpClientEngine

expect fun provideHttpEngine(): HttpClientEngine