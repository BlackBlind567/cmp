package com.hybrid.internet.data.remote

import SupportRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class SupportApi (
    private val client: HttpClient
)  {

    suspend fun submitSupportReport(request: SupportRequest): HttpResponse {
        return client.post("/api/customer/support/ticket/create") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }
}