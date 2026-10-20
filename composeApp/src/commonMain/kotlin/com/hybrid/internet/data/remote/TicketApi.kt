package com.hybrid.internet.data.remote

import com.hybrid.internet.data.model.request.SendTicketMessageRequest
import com.hybrid.internet.data.model.request.TicketConfirmationRequest
import com.hybrid.internet.data.model.request.TicketMessagesRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TicketApi(private val client: HttpClient) {

    suspend fun confirmTicket(request: TicketConfirmationRequest): HttpResponse {
        return client.post("/api/customer/support/ticket/confirmation") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun fetchTicketListFromApi(page: Int): HttpResponse {
        return client.post("/api/customer/support/tickets") {
            url {
                parameters.append("page", page.toString())
            }
        }
    }

    suspend fun fetchTicketMessages(ticketId: String): HttpResponse {
        return client.post("/api/customer/support/ticket/messages") {
            contentType(ContentType.Application.Json)
            setBody(TicketMessagesRequest(ticketId = ticketId))
        }
    }

    suspend fun sendTicketMessage(ticketId: String, message: String, image: String?): HttpResponse {
        return client.post("/api/customer/support/ticket/message/send") {
            contentType(ContentType.Application.Json)
            setBody(
                SendTicketMessageRequest(
                    ticketId = ticketId,
                    message = message,
                    image = image
                )
            )
        }
    }
}
