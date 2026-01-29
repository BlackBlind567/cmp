package com.hybrid.internet.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TicketTrackingApi (
    private val client: HttpClient
) {
    suspend fun fetchTicketHistory(
        page: Int,
        extraParams: Map<String, String> = emptyMap()
    ): HttpResponse {
        return client.post("/api/customer/support/ticket/history") {
            // 1. Pagination Query Parameter
            url {
                parameters.append("page", page.toString())
            }

            // 2. Extra Key-Value pairs in Body (JSON format)
            contentType(ContentType.Application.Json)
            setBody(extraParams)

            // Agar aapka API "Form Data" mang raha hai, toh use:
            // setBody(FormDataContent(Parameters.build {
            //    extraParams.forEach { append(it.key, it.value) }
            // }))
        }
    }
}