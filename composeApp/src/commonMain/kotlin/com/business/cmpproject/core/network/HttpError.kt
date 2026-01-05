package com.business.cmpproject.core.network

sealed class HttpError(val message: String) {
    object Unauthorized : HttpError("Session expired. Please login again.")
    object Forbidden : HttpError("You are not allowed to access this.")
    object ServerError : HttpError("Server error. Try again later.")
    object NetworkError : HttpError("No internet connection.")
    data class Unknown(val msg: String) : HttpError(msg)
}