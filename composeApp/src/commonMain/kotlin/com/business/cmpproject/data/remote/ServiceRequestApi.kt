package com.business.cmpproject.data.remote

import com.business.cmpproject.core.network.NetworkResult
import com.business.cmpproject.data.model.response.ServiceRequestPaginationResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.parameters

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