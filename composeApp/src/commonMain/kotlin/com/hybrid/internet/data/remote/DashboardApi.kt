package com.hybrid.internet.data.remote

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*

class DashboardApi(
    private val client: HttpClient
) {

    suspend fun fetchDashboard(
        deviceId: String?,
        platform: String
    ): HttpResponse {
        return client.post("/api/customer-dashboard-api") {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "device_id" to deviceId.orEmpty(),
                    "platform" to platform
                )
            )
        }
    }
}
