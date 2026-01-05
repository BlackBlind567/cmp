package com.business.cmpproject.core.network

import io.ktor.client.engine.HttpClientEngine

expect fun provideHttpEngine(): HttpClientEngine