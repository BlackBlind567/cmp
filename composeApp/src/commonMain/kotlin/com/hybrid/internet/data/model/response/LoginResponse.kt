package com.hybrid.internet.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val customer_name: String,
    val company_name: String,
//    val billing_email: String,
    val mobile: String,
    val email: String
)