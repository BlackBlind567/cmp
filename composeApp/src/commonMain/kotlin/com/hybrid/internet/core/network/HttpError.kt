package com.hybrid.internet.core.network

sealed class HttpError(val message: String) {
    object Unauthorized : HttpError("Session expired. Please login again.")
    object Forbidden : HttpError("You are not allowed to access this.")
    object ServerError : HttpError("Server error. Try again later.")
    object NetworkError : HttpError("Something went wrong in response.")
    data class TicketClosed(val reason: String) : HttpError(reason)
    data class Unknown(val msg: String) : HttpError(msg)
}
