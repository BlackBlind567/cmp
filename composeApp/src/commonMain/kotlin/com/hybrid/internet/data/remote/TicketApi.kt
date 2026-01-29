package com.hybrid.internet.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse

class TicketApi(private val client: HttpClient) {



    suspend fun fetchTicketListFromApi(): HttpResponse {
        return client.post("/api/customer/support/tickets")
//            url {
//                parameters.append("page", page.toString())
//            }
//        }
    }
}