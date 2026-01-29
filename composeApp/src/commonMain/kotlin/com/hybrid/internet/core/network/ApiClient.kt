package com.hybrid.internet.core.network

import com.hybrid.internet.core.config.AppConfig
import com.hybrid.internet.core.log.AppLogger
import io.ktor.client.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class ApiClient(
    tokenProvider: TokenProvider
) {

    val client = HttpClient(provideHttpEngine()) {

        // 🔹 Base URL
        defaultRequest {
            url(AppConfig.BASE_URL)
            contentType(ContentType.Application.Json)
        }

        // 🔹 JSON
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true   // 🔥 FIX
                    isLenient = true
                    explicitNulls = false
                }
            )
        }

        // 🔹 AUTH TOKEN (AUTO)
        install(Auth) {
            bearer {
                loadTokens {
                    tokenProvider.getToken()?.let {
                        BearerTokens(it, it)
                    }
                }
            }
        }

        // 🔥 LOG EVERYTHING
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    AppLogger.log(message)
                }
            }

            level = LogLevel.BODY
        }
    }
}
