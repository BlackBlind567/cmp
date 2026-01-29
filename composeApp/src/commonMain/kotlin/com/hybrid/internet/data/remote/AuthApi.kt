package com.hybrid.internet.data.remote

import com.hybrid.internet.data.model.request.LoginRequest
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*

class AuthApi(
    private val client: HttpClient
) {
    suspend fun login(request: LoginRequest): HttpResponse {
        return client.post("/api/customer/signin") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }


    suspend fun sendOtp(
        mobile: String
    ): HttpResponse {
        return client.post("/api/send/mobile/otp") {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "mobile" to mobile
                )
            )
        }
    }

    suspend fun verifyOtp(
        mobile: String,
        otp: String
    ): HttpResponse {
        return client.post("/api/verified/mobile/otp") {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "mobile" to mobile,
                    "otp" to otp
                )
            )
        }
    }
}