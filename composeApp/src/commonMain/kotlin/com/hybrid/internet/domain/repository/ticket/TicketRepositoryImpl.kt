package com.hybrid.internet.domain.repository.ticket

import com.hybrid.internet.data.model.request.TicketConfirmationRequest
import com.hybrid.internet.data.model.response.TicketConfirmationResponse
import com.hybrid.internet.core.network.ApiResponse
import kotlinx.coroutines.CancellationException

import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.core.network.blindApiCall
import com.hybrid.internet.core.network.HttpError
import com.hybrid.internet.core.session.SessionManager
import com.hybrid.internet.data.model.response.TicketMessageItem
import com.hybrid.internet.data.model.response.TicketPage
import com.hybrid.internet.data.remote.TicketApi
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class TicketRepositoryImpl(
    private val api: TicketApi, private val sessionManager: SessionManager
) : TicketRepository {

    override suspend fun confirmTicket(
        request: TicketConfirmationRequest
    ): NetworkResult<TicketConfirmationResponse> {
        // Confirmation changes server state: do not automatically retry the POST.
        return try {
            val response = api.confirmTicket(request)
            if (response.status == HttpStatusCode.Unauthorized) {
                sessionManager.handle(HttpError.Unauthorized)
                return NetworkResult.Failure(HttpError.Unauthorized)
            }
            val body = json.decodeFromString<ApiResponse<TicketConfirmationResponse>>(response.bodyAsText())
            val data = body.data
            if (response.status.value in 200..299 && body.success && data != null) {
                NetworkResult.Success(data)
            } else {
                NetworkResult.Failure(HttpError.Unknown(body.message ?: "Unable to confirm this ticket. Please try again."))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            NetworkResult.Failure(HttpError.NetworkError)
        }
    }

    override suspend fun getTicketList(page: Int): NetworkResult<TicketPage> {
        return blindApiCall(sessionManager) { api.fetchTicketListFromApi(page) }
    }

    override suspend fun getTicketMessages(ticketId: String): NetworkResult<List<TicketMessageItem>> {
        return when (val result = blindApiCall<JsonElement>(sessionManager) { api.fetchTicketMessages(ticketId) }) {
            is NetworkResult.Success -> NetworkResult.Success(result.data.toTicketMessages())
            is NetworkResult.Failure -> result
        }
    }

    override suspend fun sendTicketMessage(ticketId: String, message: String, image: String?): NetworkResult<Unit> {
        return try {
            val response = api.sendTicketMessage(ticketId, message, image)
            val raw = response.bodyAsText()
            if (response.status != HttpStatusCode.Unauthorized) {
                val body = runCatching { json.parseToJsonElement(raw).jsonObject }.getOrNull()
                closedTicketError(body)?.let { return NetworkResult.Failure(it) }
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val root = json.parseToJsonElement(raw).jsonObject
                    val isSuccess = root["success"]?.jsonPrimitive?.booleanOrNull == true
                    if (isSuccess) {
                        NetworkResult.Success(Unit)
                    } else {
                        NetworkResult.Failure(
                            HttpError.Unknown(
                                root["message"]?.jsonPrimitive?.contentOrNull
                                    ?: "Failed to send message"
                            )
                        )
                    }
                }

                HttpStatusCode.Unauthorized -> {
                    val error = HttpError.Unauthorized
                    sessionManager.handle(error)
                    NetworkResult.Failure(error)
                }

                HttpStatusCode.BadRequest -> {
                    val root = runCatching { json.parseToJsonElement(raw).jsonObject }.getOrNull()
                    NetworkResult.Failure(
                        HttpError.Unknown(
                            root?.get("message")?.jsonPrimitive?.contentOrNull
                                ?: "Failed to send message"
                        )
                    )
                }

                else -> NetworkResult.Failure(HttpError.Unknown("Failed to send message"))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            NetworkResult.Failure(HttpError.NetworkError)
        }
    }

    private fun JsonElement.toTicketMessages(): List<TicketMessageItem> {
        return when (this) {
            is JsonArray -> parseMessages(this)
            is JsonObject -> {
                val messages = this["messages"]
                    ?: this["ticket_messages"]
                    ?: this["ticketMessages"]
                    ?: this["data"]

                when (messages) {
                    is JsonArray -> parseMessages(messages)
                    is JsonObject -> listOf(json.decodeFromJsonElement(messages))
                    else -> listOf(json.decodeFromJsonElement(this))
                }
            }
            else -> emptyList()
        }
    }

    private fun parseMessages(items: JsonArray): List<TicketMessageItem> {
        return items.mapNotNull { item ->
            runCatching {
                json.decodeFromJsonElement<TicketMessageItem>(item)
            }.getOrNull()
        }
    }

    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        }
    }
}
