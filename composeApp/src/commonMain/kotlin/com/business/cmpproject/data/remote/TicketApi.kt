package com.business.cmpproject.data.remote

import com.business.cmpproject.data.model.request.LoginRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TicketApi(private val client: HttpClient) {

    suspend fun login(request: LoginRequest): HttpResponse {
        return client.post("/api/customer/signin") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }
}