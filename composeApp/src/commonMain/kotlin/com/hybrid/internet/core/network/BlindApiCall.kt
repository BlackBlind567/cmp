package com.hybrid.internet.core.network

import com.hybrid.internet.core.log.AppLogger
import com.hybrid.internet.core.log.AppLogger.e
import com.hybrid.internet.core.session.SessionManager
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend inline fun <reified T> blindApiCall(
    sessionManager: SessionManager,
    crossinline apiCall: suspend () -> HttpResponse
): NetworkResult<T> {

    return withContext(Dispatchers.IO) {
        try {

            retry {
                val response = apiCall()

                AppLogger.network(
                    "URL=${response.request.url} | STATUS=${response.status}"
                )

                when (response.status) {

                    HttpStatusCode.OK -> {
                        val body = response.body<ApiResponse<T>>()
                        if (body.success && body.data != null)
                            NetworkResult.Success(body.data)
                        else
                            NetworkResult.Failure(
                                HttpError.Unknown(body.message!!)
                            )
                    }

                    HttpStatusCode.BadRequest -> {

                        val raw = response.body<String>()

                        val json = Json { ignoreUnknownKeys = true }

                        val root = json.parseToJsonElement(raw).jsonObject

                        val dataObject = root["data"]?.jsonObject

                        // ✅ Try to read "error" field first (new API format)
                        val directError = dataObject
                            ?.get("error")
                            ?.jsonPrimitive
                            ?.contentOrNull

                        // ✅ Try old validation format (array of errors)
                        val firstValidationError = dataObject
                            ?.values
                            ?.firstOrNull()
                            ?.let { element ->
                                when {
                                    element is kotlinx.serialization.json.JsonArray ->
                                        element.firstOrNull()?.jsonPrimitive?.contentOrNull
                                    element is kotlinx.serialization.json.JsonPrimitive ->
                                        element.contentOrNull
                                    else -> null
                                }
                            }

                        val message =
                            directError
                                ?: firstValidationError
                                ?: root["message"]?.jsonPrimitive?.contentOrNull
                                ?: "Validation error"

                        println("API BadRequest ==> $message")

                        NetworkResult.Failure(HttpError.Unknown(message))
                    }


                    HttpStatusCode.Unauthorized -> {
                        val error = HttpError.Unauthorized
                        sessionManager.handle(error)
                        NetworkResult.Failure(HttpError.Unauthorized)
                    }

                    HttpStatusCode.Forbidden ->
                        NetworkResult.Failure(HttpError.Forbidden)

                    in HttpStatusCode.InternalServerError..HttpStatusCode.GatewayTimeout ->
                        NetworkResult.Failure(HttpError.ServerError)

                    else ->
                        NetworkResult.Failure(
                            HttpError.Unknown("Unexpected error")
                        )
                }
            }

        } catch (e: Exception) {
            AppLogger.e(
                "API",
                e.message ?: "Unknown error"
            )
            NetworkResult.Failure(HttpError.NetworkError)
        }
    }
}
