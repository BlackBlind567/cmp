package com.hybrid.internet.core.network

import com.hybrid.internet.core.log.AppLogger
import com.hybrid.internet.core.log.AppLogger.e
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <reified T> blindApiCall(
    crossinline apiCall: suspend () -> HttpResponse
): NetworkResult<T> {

    return withContext(Dispatchers.Default) {
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
                                HttpError.Unknown(body.message)
                            )
                    }

                    HttpStatusCode.BadRequest -> {
                        val body = response.body<ApiResponse<T>>()
                        NetworkResult.Failure(HttpError.Unknown(body.message))
                    }


                    HttpStatusCode.Unauthorized ->
                        NetworkResult.Failure(HttpError.Unauthorized)

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
