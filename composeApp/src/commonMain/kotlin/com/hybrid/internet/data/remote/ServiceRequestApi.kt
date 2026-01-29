package com.hybrid.internet.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

class ServiceRequestApi(private val client: HttpClient) {

    suspend fun getServiceRequests(page: Int): HttpResponse {
        return client.post("api/customer/requests"){
            url {
                parameters.append("page", page.toString())
            }
        }
    }

    suspend fun submitRequest(params: MutableMap<String, String>): HttpResponse {
        return client.post("/api/customer/requests/store"){
            setBody(params)
        }
    }
}